package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendCommandServiceImpl implements RecommendCommandService{
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final RecommendQueryService recommendQueryService;

    //GPT 관련 변수
    @Value("${chatgpt.api-key}")
    private String API_KEY;
    @Value("${chatgpt.message-url}")
    private String API_URL;
    @Override
    public RecommendResponse.RecommendResponseDTO requestRecommend(Long memberID, RecommendRequest.RecommendRequestDTO recommendRequestDTO) {
        // 사용자 선호 정보 추출을 위한 Member 객체 생성
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));

        //GPT API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        //GPT API 요청 바디 설정
        Map<String, Object> body = generatePrompt(member, recommendRequestDTO, 0.5f, 300);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        //GPT API 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
        Map responseBody = response.getBody();

        //GPT API 응답에서 추천 결과 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = message.get("content").toString();

        //GPT API 응답에서 주종, 이유 추출
        String drinkType = content.split("\n")[0];
        String reason = content.split("\n")[1];

        //추천 결과 DB에 저장
        recommendQueryService.addRecommend(member, recommendRequestDTO, drinkType, reason);

        //ResponseDTO 생성 및 return
        return RecommendResponse.RecommendResponseDTO.builder()
                .drinkName(drinkType)
                .foodName(recommendRequestDTO.getFoodName())
                .recommendReason(reason)
                .build();
    }

    private Map<String, Object> generatePrompt(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO, float temperature, int max_token) {
        // GPT 프롬프트 엔지니어링 부분
        List<RecommendRequest.GPTMessage> messages = new ArrayList<>();
        // GPT에 역할 설명
        String roleExplain = "You are someone who knows best which type of alcohol pairs well with various foods. " +
                "You have traveled around the world, " +
                "tasting many foods and drinks, and you understand these pairings better than anyone else. " +
                "Additionally, you have a precise understanding of the types of alcohol that Korean people enjoy. " +
                "You recommend people to the type of alcohol that best goes well with their food.";
        messages.add(RecommendRequest.GPTMessage.builder()
                .role("system")
                .content(roleExplain)
                .build());

        //GPT에 유저 정보 제공
        String userInfo = generateUserInfo(member, recommendRequestDTO);
        messages.add(RecommendRequest.GPTMessage.builder()
                .role("assistant")
                .content(userInfo)
                .build());
        //GPT에 답변 형식 제공
        messages.add(RecommendRequest.GPTMessage.builder()
                .role("assistant")
                .content("The response format has two sections, each sections are separated by \"\\n\". The first is <Type of Alcohol>, " +
                        "and the second is <Reason for Recommendation>. In the <Type of Alcohol> section, " +
                        "please write only the recommended type of alcohol, without any additional words. " +
                        "In the <Reason for Recommendation> section, please write about 3-4 lines explaining why you recommend it. " +
                        "All responses should be in Korean.")
                .build());
        //GPT에 질문
        messages.add(RecommendRequest.GPTMessage.builder()
                .role("user")
                .content( "Please recommend one type of alcohol that pairs best with the dish they will be eating and provide a 3~4 sentence explanation."+
                        "You should response in Korean.")
                .build());

        Map<String, Object> prompt = new HashMap<>();
        prompt.put("model", "gpt-3.5-turbo");
        prompt.put("messages",messages);
        prompt.put("temperature", temperature);
        prompt.put("max_tokens", max_token);

        return prompt;
    }

    //유저 입력 정보를 GPT에 제공하기 위한 문자열 생성 메소드
    private String generateUserInfo(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO) {
        // 필수 정보
        //순서: 선호 주종, 선호 도수, 주량, 음주 횟수, 취하고 싶은 정도, 음식 이름
        String userInfo = String.format("The preferred type of alcohol for the person you are recommending is \"%s\". " +
                "Their preferred alcohol content is \"%s\". " +
                "Their drinking capacity is \"%s\". " +
                "The frequency of their drinking is \"%s\". " +
                "They wish to be intoxicated to a level \"%s\" out of 5. " +
                "The dish they will be eating is \"%s\". ", member.getPreferredAlcoholType(), member.getPreferredAlcoholDegree()
        , member.getDrinkingLimit(), member.getDrinkingTimes(), recommendRequestDTO.getDesireLevel(), recommendRequestDTO.getFoodName());

        //선택 정보 입력
        //기분
        if(recommendRequestDTO.getFeeling() != null)
            userInfo += String.format("Their mood is \"%s\". ", recommendRequestDTO.getFeeling());
        //날씨
        if(recommendRequestDTO.getWeather() != null)
            userInfo += String.format("The current weather is \"%s\". ", recommendRequestDTO.getWeather());

        return userInfo;
    }


}

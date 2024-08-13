package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recommend.Recommend;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import com.example.dgbackend.global.s3.dto.S3Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecommendCommandServiceImpl implements RecommendCommandService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final RecommendQueryService recommendQueryService;
    private final S3Service s3Service;

    //GPT 관련 변수
    @Value("${chatgpt.api-key}")
    private String API_KEY;
    @Value("${chatgpt.chat-url}")
    private String API_CHAT_URL;
    @Value("${chatgpt.chat-model}")
    private String API_CHAT_MODEL;
    @Value("${chatgpt.image-url}")
    private String API_IMAGE_URL;
    @Value("${chatgpt.image-model}")
    private String API_IMAGE_MODEL;
    @Value("${chatgpt.image-size}")
    private String API_IMAGE_SIZE;

    /*
    주류 추천 요청
    memberID : 추천 요청을 보내는 사용자의 ID (TODO: 소셜로그인 통합시 MemberID를 Token에서 추출)
    recommendRequestDTO : 추천 요청 정보
     */
    @Override
    public RecommendResponse.RecommendResponseDTO requestRecommend(Member member,
        RecommendRequest.RecommendRequestDTO recommendRequestDTO) {
        if (recommendRequestDTO.getDesireLevel() == null) {
            throw new ApiException(ErrorStatus._NULL_DESIRE_LEVEL);
        }
        if (recommendRequestDTO.getFoodName() == null) {
            throw new ApiException(ErrorStatus._NULL_FOOD_NAME);
        }

        // 사용자 선호 정보 추출을 위한 Member 객체 생성
//        Member member = memberRepository.findById(memberID).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));

        //GPT API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        //GPT API 요청 바디 설정
        Map<String, Object> body = generateChatPrompt(member, recommendRequestDTO, 0.5f, 300);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        //GPT API 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(API_CHAT_URL, request, Map.class);
        Map responseBody = response.getBody();

        //GPT API 응답에서 추천 결과 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = message.get("content").toString();

        //GPT API 응답에서 주종, 이유 추출
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> gptResult = new HashMap<String, String>();
        try {
            gptResult = mapper.readValue(content, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String drinkType = gptResult.get("Alcohol");
        String reason = gptResult.get("Reason");

        //추천 결과 이미지 생성
        String imageUrl = makeCombinationImage(member, drinkType, recommendRequestDTO);
        //추천 결과 DB에 저장
        Recommend recommend = recommendQueryService.addRecommend(member, recommendRequestDTO,
            drinkType, reason, imageUrl);

        //ResponseDTO 생성 및 return
        return RecommendResponse.RecommendResponseDTO.builder()
            .recommendID(recommend.getId())
            .drinkName(drinkType)
            .foodName(recommendRequestDTO.getFoodName())
            .recommendReason(reason)
            .imageUrl(imageUrl)
            .build();
    }

    /*
    주류 추천 요청에 사용할 프롬프트 생성 메소드
    member : 추천 요청을 보내는 사용자의 Member 객체
    recommendRequestDTO : 추천 요청 정보
    temperature : GPT API temperature parameter. 높을수록 수록 다양한 문장 생성. 높을수록 생성 속도 저하 (0.0 ~ 2.0)
    max_token : GPT API max_token parameter. 생성할 최대 문장 길이
     */
    private Map<String, Object> generateChatPrompt(Member member,
        RecommendRequest.RecommendRequestDTO recommendRequestDTO, float temperature,
        int max_token) {
        // GPT 프롬프트 엔지니어링 부분
        List<RecommendRequest.GPTMessage> messages = new ArrayList<>();
        // GPT에 역할 설명 (페르소나 부여)
        String roleExplain =
            "You are someone who knows best which type of alcohol pairs well with various foods. " +
                "You have traveled around the world, " +
                "tasting many foods and drinks, and you understand these pairings better than anyone else. "
                +
                "Additionally, you have a precise understanding of the types of alcohol that Korean people enjoy. "
                +
                "You recommend people to the type of alcohol that best goes well with their food.";
        messages.add(RecommendRequest.GPTMessage.builder()
            .role("system")
            .content(roleExplain)
            .build());
        //GPT에 답변 형식 제공 (Form)
        messages.add(RecommendRequest.GPTMessage.builder()
            .role("system")
            .content(
                "\"You are tasked with recommending the type of alcohol that best pairs with the user's chosen food. "
                    +
                    "When making the recommendation, you consider the preferred type of alcohol, " +
                    "preferred alcohol strength, drinking capacity, frequency of drinking, " +
                    "desired level of intoxication, \"name of the food\", mood, and weather. " +
                    "The result is produced in JSON format with two components: \"Alcohol\" and \"Reason\". "
                    +
                    "In \"Alcohol\", only the name of the alcohol is written. The \"Reason\" consists of a 3-4 line explanation. "
                    +
                    "Please respond only values in Korean.\"")
            .build());
        //GPT에 유저 정보 제공 (Context)
        String userInfo = generateUserInfo(member, recommendRequestDTO);
        messages.add(RecommendRequest.GPTMessage.builder()
            .role("system")
            .content(userInfo)
            .build());
        //GPT에 질문 (작업)
        messages.add(RecommendRequest.GPTMessage.builder()
            .role("user")
            .content(
                "Please recommend one type of alcohol that pairs best with the dish they will be eating and provide a 3~4 sentence explanation.")
            .build());

        Map<String, Object> prompt = new HashMap<>();
        prompt.put("model", API_CHAT_MODEL);
        prompt.put("messages", messages);

//        Map<String, String> formatType = new HashMap<>();
//        formatType.put("type", "json_object");
//        prompt.put("response_format", formatType);
        prompt.put("temperature", temperature);
        prompt.put("max_tokens", max_token);

        return prompt;
    }

    /*
    추천받은 음식 주류 조합 이미지 생성 메소드
    member : 추천 요청을 보내는 사용자의 Member 객체
    drinkType : 추천된 주종
    requestDTO : 추천 요청 정보
     */
    public String makeCombinationImage(Member member, String drinkName,
        RecommendRequest.RecommendRequestDTO requestDTO) {
        //Dall e API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        //Dall e API 요청 바디 설정
        String prompt = generateImagePrompt(member, requestDTO.getFoodName(), drinkName,
            requestDTO.getFeeling(), requestDTO.getWeather());

        Map<String, Object> body = new HashMap<>();
        body.put("model", API_IMAGE_MODEL);
        body.put("prompt", prompt);
        body.put("n", 1);
        body.put("size", API_IMAGE_SIZE);
        body.put("response_format", "b64_json");
        body.put("style", "vivid");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        //Dall e API 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(API_IMAGE_URL, request,
            Map.class);
        Map responseBody = response.getBody();

        //Dall e API 응답에서 추천 결과 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("data");

        //B64_json -> MultipartFile
        byte[] imageByte = Base64.getDecoder().decode((String) choices.get(0).get("b64_json"));
        MultipartFile imageFile = new BASE64DecodedMultipartFile(imageByte, "image/png",
            "image.png");

        //S3에 이미지 업로드
        List<S3Result> S3UploadResult = s3Service.uploadFile(Collections.singletonList(imageFile));

        return S3UploadResult.get(0).getImgUrl();
    }

    @Override
    public void deleteCancellation(Member member) {

        List<Recommend> recommends = recommendRepository.findAllByMember(member);
        List<String> imageUrls = new ArrayList<>();

        for (Recommend rc : recommends) {
            imageUrls.add(rc.getImageUrl());
        }

//        List<String> imageUrls = recommends.stream()
//                .map(Recommend::getImageUrl)
//                .filter(Objects::nonNull)
//                .toList();

        imageUrls.forEach(s3Service::deleteFile);

        recommendRepository.deleteAllByMember(member);
    }

    /*
    이미지 생성에 사용할 프롬프트 생성 메소드
    member : 추천 요청을 보내는 사용자의 Member 객체
    foodName : 음식 이름
    drinkyType : 추천된 주종
    mood : 유저 기분
    weather : 현재 날씨
     */
    private String generateImagePrompt(Member member, String foodName, String drinkyType,
        String mood, String weather) {
        String prompt = "Things to consider for creation: Photorealism, "
            + "Food and drink combinations, "
            + "Setting and atmosphere, "
            + "Visual Characteristics of the Food, "
            + "Sentimental Value.\n";
        if (mood != null) {
            prompt += String.format("The user's mood is \"%s\". ", mood);
        }
//        if (weather != null)      // 사용하지 않지만 혹시몰라 남겨둔다.
//            prompt += String.format("The current weather is \"%s\". ", weather);

        prompt += String.format("The combination is \"%s\" and \"%s\". Make an image please.",
            foodName, drinkyType);

        return prompt;
    }

    //유저 입력 정보를 GPT에 제공하기 위한 문자열 생성 메소드
    private String generateUserInfo(Member member,
        RecommendRequest.RecommendRequestDTO recommendRequestDTO) {
        // 필수 정보
        //순서: 선호 주종, 선호 도수, 주량, 음주 횟수, 취하고 싶은 정도, 음식 이름
        String userInfo = String.format("The user's preferred type of alcohol: \"%s\". " +
                "The degree of intoxication the user desires: \"%s\". " +
                "The user's drinking capacity: \"%s\". " +
                "The user's frequency of drinking: \"%s\". " +
                "The degree of intoxication the user wants to achieve (1~5): \"%s\". " +
                "The food the user will eat: \"%s\". ", member.getPreferredAlcoholType(),
            member.getPreferredAlcoholDegree()
            , member.getDrinkingLimit(), member.getDrinkingTimes(),
            recommendRequestDTO.getDesireLevel(), recommendRequestDTO.getFoodName());

        //선택 정보 입력
        //기분
        if (recommendRequestDTO.getFeeling() != null) {
            userInfo += String.format("The user's mood is \"%s\". ",
                recommendRequestDTO.getFeeling());
        }
        //날씨
        if (recommendRequestDTO.getWeather() != null) {
            userInfo += String.format("The current weather is \"%s\". ",
                recommendRequestDTO.getWeather());
        }

        return userInfo;
    }

}

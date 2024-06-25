package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.combination.service.CombinationCommandService;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentCommandService;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeCommandService;
import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    @Autowired
    MemberRepository memberRepository;

    private final S3Service s3Service;
    private final CombinationCommandService combinationCommandService;
    private final CombinationCommentCommandService combinationCommentCommandService;
    private final CombinationLikeCommandService combinationLikeCommandService;

    @Override
    public MemberResponse.RecommendInfoDTO patchRecommendInfo(Member member,
        MemberRequest.RecommendInfoDTO requestInfoDTO) {
        member.setPreferredAlcoholType(requestInfoDTO.getPreferredAlcoholType());
        member.setPreferredAlcoholDegree(requestInfoDTO.getPreferredAlcoholDegree());
        member.setDrinkingTimes(requestInfoDTO.getDrinkingTimes());
        member.setDrinkingLimit(requestInfoDTO.getDrinkingLimit());

        memberRepository.save(member);

        return MemberResponse.toRecommendInfoDTO(member);
    }

    // 회원 정보 수정
    @Override
    public MemberResponse.GetMember patchMember(Member member,
                                                MemberRequest.PatchMember patchMember) {
        member.setName(patchMember.getName());
        member.setNickName(patchMember.getNickName());
        member.setBirthDate(patchMember.getBirthDate());
        member.setPhoneNumber(patchMember.getPhoneNumber());
        member.setGender(patchMember.getGender());

        return MemberResponse.toGetMember(member);
    }

    //회원 사진 수정
    @Override
    public MemberResponse.GetMember patchProfileImage(Member member,
                                                      MultipartFile multipartFile) {
        String originUrl = member.getProfileImageUrl();

        if (originUrl != null) {
            s3Service.deleteFile(originUrl);
        }
        String profileImageUrl = (s3Service.uploadOneFile(multipartFile).getImgUrl());
        member.updateProfileImageUrl(profileImageUrl);

        return MemberResponse.toGetMember(member);
    }

    @Override
    public MemberResponse.GetMember reportedMember(Member member) {
        member.setState(State.REPORTED);

        return MemberResponse.toGetMember(member);
    }

    @Override
    public MemberResponse.GetMember getMember(Member member) {
        return MemberResponse.toGetMember(member);
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}


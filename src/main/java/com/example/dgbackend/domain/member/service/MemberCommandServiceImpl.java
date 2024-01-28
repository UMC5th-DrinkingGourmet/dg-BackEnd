package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {
    @Autowired
    MemberRepository memberRepository;

    private final S3Service s3Service;

    @Override
    public MemberResponse.RecommendInfoDTO patchRecommendInfo(Long memberID, MemberRequest.RecommendInfoDTO requestInfoDTO) {
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        member.setPreferredAlcoholType(requestInfoDTO.getPreferredAlcoholType());
        member.setPreferredAlcoholDegree(requestInfoDTO.getPreferredAlcoholDegree());
        member.setDrinkingTimes(requestInfoDTO.getDrinkingTimes());
        member.setDrinkingLimit(requestInfoDTO.getDrinkingLimit());

        memberRepository.save(member);

        return MemberResponse.toRecommendInfoDTO(member);
    }

    // 회원 정보 수정
    @Override
    public MemberResponse.GetMember patchMember(Long memberId, MemberRequest.PatchMember patchMember) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        String originUrl = member.getProfileImageUrl();

        if (patchMember.getProfileImage() != null) {
            s3Service.deleteFile(originUrl);
            String profileImageUrl = (s3Service.uploadOneFile(patchMember.getProfileImage())).getImgUrl();

            member.update(patchMember.getName(), patchMember.getNickName(), patchMember.getBirthDate(), patchMember.getPhoneNumber(), profileImageUrl, patchMember.getGender());
        } else {
            member.update(patchMember.getName(), patchMember.getNickName(), patchMember.getBirthDate(), patchMember.getPhoneNumber(), originUrl, patchMember.getGender());
        }

        return MemberResponse.toGetMember(member);
    }

    // 회원 탈퇴
    @Override
    public String patchSignOut(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        member.signout();

        memberRepository.save(member);

        return "회원 탈퇴가 완료되었습니다.";
    }
}

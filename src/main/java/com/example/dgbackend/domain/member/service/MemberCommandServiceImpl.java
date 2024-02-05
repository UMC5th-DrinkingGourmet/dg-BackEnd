package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.combination.service.CombinationCommandService;
import com.example.dgbackend.domain.combination.service.CombinationCommandServiceImpl;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentCommandService;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeCommandService;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recipe.service.RecipeService;
import com.example.dgbackend.domain.recipecomment.service.RecipeCommentService;
import com.example.dgbackend.domain.recipelike.RecipeLike;
import com.example.dgbackend.domain.recipelike.service.RecipeLikeService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
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
    private final RecipeService recipeCommandService;
    private final RecipeCommentService recipeCommentService;
    private final RecipeLikeService recipeLikeCommandService;

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

        member.update(patchMember.getName(), patchMember.getNickName(), patchMember.getBirthDate(), patchMember.getPhoneNumber(), patchMember.getGender());


        return MemberResponse.toGetMember(member);
    }

    //회원 사진 수정
    @Override
    public MemberResponse.GetMember patchProfileImage(Long memberId, MultipartFile multipartFile) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        String originUrl = member.getProfileImageUrl();

        if (originUrl != null) {
            s3Service.deleteFile(originUrl);
            String profileImageUrl = (s3Service.uploadOneFile(multipartFile).getImgUrl());

            member.updateProfileImageUrl(profileImageUrl);
        }

        return MemberResponse.toGetMember(member);
    }

    // 회원 탈퇴
    @Override
    public String patchSignOut(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        member.signout();

        combinationCommandService.deleteAllCombination(memberId);
        combinationCommentCommandService.deleteAllComment(memberId);
        combinationLikeCommandService.deleteAllLike(memberId);

        recipeCommandService.deleteAllRecipe(memberId);
        recipeLikeCommandService.deleteAllRecipeLike(memberId);
        recipeCommentService.deleteAllRecipeComment(memberId);

        memberRepository.deleteById(memberId);

        return "회원 탈퇴가 완료되었습니다.";

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}

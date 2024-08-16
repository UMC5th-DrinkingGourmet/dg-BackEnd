package com.example.dgbackend.domain.cancellation.service;

import com.example.dgbackend.domain.cancellation.Cancellation;
import com.example.dgbackend.domain.cancellation.repository.CancellationRepository;
import com.example.dgbackend.domain.combination.service.CombinationCommandService;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentCommandService;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeCommandService;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.service.MemberCommandService;
import com.example.dgbackend.domain.memberblock.service.MemberBlockService;
import com.example.dgbackend.domain.recipe.service.RecipeService;
import com.example.dgbackend.domain.recipecomment.service.RecipeCommentService;
import com.example.dgbackend.domain.recipelike.service.RecipeLikeService;
import com.example.dgbackend.domain.recommend.service.RecommendCommandService;
import com.example.dgbackend.domain.report.service.ReportService;
import com.example.dgbackend.domain.termagree.service.TermAgreeCommandService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CancellationSchedular {

    private final CancellationRepository cancellationRepository;
    private final CombinationCommandService combinationCommandService;
    private final RecipeService recipeService;
    private final CombinationCommentCommandService combinationCommentCommandService;
    private final RecipeCommentService recipeCommentService;
    private final CombinationLikeCommandService combinationLikeCommandService;
    private final RecipeLikeService recipeLikeService;
    private final RecommendCommandService recommendCommandService;
    private final TermAgreeCommandService termAgreeCommandService;
    private final MemberCommandService memberCommandService;
    private final MemberBlockService memberBlockService;
    private final ReportService reportService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void processCancellations() {

        LocalDateTime now = LocalDateTime.now();
        List<Cancellation> expiredCancellations = cancellationRepository.findAllByCancelledAtBefore(
            now);

        for (Cancellation cancellation : expiredCancellations) {
            runCancellation(cancellation.getId());
        }
    }

    private void runCancellation(Long cancellationId) {

        Cancellation cancellation = cancellationRepository.findById(cancellationId)
            .orElseThrow(() -> new ApiException(ErrorStatus._CANCELLATION_NOT_FOUND));

        Member cancelMember = cancellation.getMember();
        Long memberId = cancellation.getMember().getId();

        // 탈퇴 로직 실행
        // 추천 조합 삭제
        recommendCommandService.deleteCancellation(memberId);
        // 작성한 오늘의 조합 삭제
        combinationCommandService.deleteAllCombination(memberId);
        // 작성한 레시피 삭제
        recipeService.deleteAllRecipe(memberId);
        // 작성한 오늘의 조합 댓글 바꾸기
        combinationCommentCommandService.changeAllCombinationComment(memberId);
        // 작성한 레시피북 댓글 바꾸기
        recipeCommentService.changeAllRecipeComment(memberId);
        // 오늘의 조합 좋아요 기록 삭제
        combinationLikeCommandService.deleteCancellation(cancelMember);
        // 레시피북 좋아요 기록 삭제
        recipeLikeService.deleteCancellation(cancelMember);
        // 약관 기록 삭제
        termAgreeCommandService.deleteCancellation(cancelMember);
        // 차단 기록 삭제
        memberBlockService.deleteBlock(cancelMember);
        // 신고 기록 삭제
        reportService.deleteReport(memberId);

        // 탈퇴 테이블에서 해당 엔티티 삭제
        cancellationRepository.delete(cancellation);

        // 멤버 삭제
        memberCommandService.finalDeleteMember(memberId);
    }
}

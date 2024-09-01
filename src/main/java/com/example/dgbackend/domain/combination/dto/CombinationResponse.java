package com.example.dgbackend.domain.combination.dto;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


public class CombinationResponse {

    /**
     * 오늘의 조합 홈 페이지 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationPreviewResultList {

        List<CombinationPreviewResult> combinationList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationPreviewResult {

        Long combinationId;
        String title;
        String combinationImageUrl;
        Long likeCount;
        Long commentCount;
        List<String> hashTageList;
        Boolean isLike;
    }

    // Page<Combination> -> Page<CombinationPreviewDTO> 로 변환
    public static CombinationPreviewResultList toCombinationPreviewResultList(
        Page<Combination> combinations,
        List<List<HashTagOption>> hashTagOptions,
        List<Boolean> isLikes) {

        List<CombinationPreviewResult> combinationPreviewDTOS = IntStream.range(0,
                combinations.getContent().size())
            .mapToObj(i -> toCombinationPreviewResult(combinations.getContent().get(i),
                hashTagOptions.get(i), isLikes.get(i)))
            .collect(Collectors.toList());

        return CombinationPreviewResultList.builder()
            .combinationList(combinationPreviewDTOS)
            .listSize(combinationPreviewDTOS.size())
            .totalPage(combinations.getTotalPages())
            .totalElements(combinations.getTotalElements())
            .isFirst(combinations.isFirst())
            .isLast(combinations.isLast())
            .build();
    }

    // Combination -> CombinationPreviewDTO로 변환
    public static CombinationPreviewResult toCombinationPreviewResult(Combination combination,
        List<HashTagOption> hashTagOptions,
        Boolean isLike) {
        // TODO: 대표 이미지 정하기
        String imageUrl = combination.getCombinationImages()
            .stream()
            .findFirst()
            .map(CombinationImage::getImageUrl)
            .orElse(null);

        // 해시태그 정보
        List<String> hashTagList = hashTagOptions.stream()
            .map(hto -> hto.getHashTag().getName())
            .collect(Collectors.toList());

        return CombinationPreviewResult.builder()
            .combinationId(combination.getId())
            .title(combination.getTitle())
            .combinationImageUrl(imageUrl)
            .likeCount(combination.getLikeCount())
            .commentCount(combination.getCommentCount())
            .hashTageList(hashTagList)
            .isLike(isLike)
            .build();
    }

    /**
     * 오늘의 조합 마이페이지 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationMyPageList {

        List<CombinationMyPage> combinationList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    /**
     * 오늘의 조합 메인 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationMainList {

        List<CombinationMyPage> combinationList;
    }

    /**
     * 내가 작성한 오늘의 조합 내가 좋아요한 오늘의 조합 메인 랜덤 5개 오늘의 조합
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationMyPage {

        Long combinationId;
        String title;
        String combinationImageUrl;
    }

    public static CombinationMyPageList toCombinationMyPageList(Page<Combination> combinations) {

        List<CombinationMyPage> combinationMyPages = combinations.getContent()
            .stream()
            .map(combo -> toCombinationMyPage(combo))
            .collect(Collectors.toList());

        return CombinationMyPageList.builder()
            .combinationList(combinationMyPages)
            .listSize(combinationMyPages.size())
            .totalPage(combinations.getTotalPages())
            .totalElements(combinations.getTotalElements())
            .isFirst(combinations.isFirst())
            .isLast(combinations.isLast())
            .build();
    }

    public static CombinationMainList toCombinationMainList(List<Combination> combinations,
        List<CombinationImage> imgs) {

        List<CombinationMyPage> combinationMyPages = combinations
            .stream()
            .map(combo -> toCombinationMain(combo, imgs))
            .collect(Collectors.toList());

        return CombinationMainList.builder()
            .combinationList(combinationMyPages)
            .build();
    }

    public static CombinationMyPage toCombinationMyPage(Combination combination) {
        // TODO: 대표 이미지 정하기
        String imageUrl = combination.getCombinationImages()
            .stream()
            .findFirst()
            .map(CombinationImage::getImageUrl)
            .orElse(null);

        return CombinationMyPage.builder()
            .combinationId(combination.getId())
            .title(combination.getTitle())
            .combinationImageUrl(imageUrl)
            .build();
    }

    public static CombinationMyPage toCombinationMain(Combination combination,
        List<CombinationImage> imgList) {
        // TODO: 대표 이미지 정하기

        String imageUrl = imgList.stream()
            .filter(img -> img != null && img.getCombination().getId().equals(combination.getId()))
            .findAny()  // 이미지 중 첫 번째 것만 가져옴
            .map(img -> img.getImageUrl())
            .orElse(null);  // 만약 이미지가 없다면 null 반환

        return CombinationMyPage.builder()
            .combinationId(combination.getId())
            .title(combination.getTitle())
            .combinationImageUrl(imageUrl)
            .build();
    }


    /**
     * 오늘의 조합 상세 정보 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationDetailResult {

        CombinationResult combinationResult;
        MemberResponse.MemberResult memberResult;
        CombinationCommentResponse.CommentPreViewResult combinationCommentResult;
    }

    public static CombinationDetailResult toCombinationDetailResult(
        CombinationResult combinationResult,
        MemberResponse.MemberResult memberResult,
        CombinationCommentResponse.CommentPreViewResult combinationCommentResult) {

        return CombinationDetailResult.builder()
            .combinationResult(combinationResult)
            .memberResult(memberResult)
            .combinationCommentResult(combinationCommentResult)
            .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonInclude(Include.NON_NULL) // null 값이 아닐 때만 반환
    public static class CombinationResult {

        Long combinationId;
        String title;
        String content;
        List<String> hashTagList;
        List<String> combinationImageList;
        Boolean isCombinationLike;
        Long recommendId;
        String recommend;
    }

    public static CombinationResult toCombinationResult(Combination combination,
        List<HashTagOption> hashTagOptions,
        boolean isCombinationLike) {
        List<String> hashTagList = hashTagOptions.stream()
            .map(hto -> hto.getHashTag().getName())
            .toList();

        List<String> imageList = combination.getCombinationImages().stream()
            .map(CombinationImage::getImageUrl)
            .toList();

        return CombinationResult.builder()
            .combinationId(combination.getId())
            .title(combination.getTitle())
            .content(combination.getContent())
            .hashTagList(hashTagList)
            .combinationImageList(imageList)
            .isCombinationLike(isCombinationLike)
            .recommendId(combination.getRecommend() != null ? combination.getRecommend().getId() : null)
            .recommend(combination.getRecommend().getFoodName() + " & " +combination.getRecommend().getDrinkName())
            .build();
    }


    /**
     * 오늘의 조합 수정 정보 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationEditResult {

        String title;
        String content;
        List<String> hashTagList;
        List<String> combinationImageUrlList;
        Long recommendId;
    }

    public static CombinationEditResult toCombinationEditResult(Combination combination,
        List<HashTagOption> hashTagOptions,
        List<CombinationImage> combinationImages) {

        List<String> hashTagList = hashTagOptions.stream()
            .map(hto -> hto.getHashTag().getName())
            .toList();

        List<String> combinationImageUrlList = combinationImages.stream()
            .map(CombinationImage::getImageUrl)
            .toList();

        return CombinationEditResult.builder()
            .title(combination.getTitle())
            .content(combination.getContent())
            .hashTagList(hashTagList)
            .combinationImageUrlList(combinationImageUrlList)
            .recommendId(combination.getRecommend().getId())
            .build();
    }

    /**
     * 작성, 수정, 삭제시 응답 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationProcResult {

        Long combinationId;
        LocalDateTime createdAt;
    }

    public static CombinationProcResult toCombinationProcResult(Long combinationId) {
        return CombinationProcResult.builder()
            .combinationId(combinationId)
            .createdAt(LocalDateTime.now())
            .build();
    }

    /**
     * 오늘의 조합 메인 미리보기 DTO
     */

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationMainPreview {

        Long combinationId;
        String title;
        String combinationImageUrl;
        List<String> hashTagList;
    }

    /**
     * 오늘의 조합 메인 미리보기 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationMainPreviewList {

        List<CombinationMainPreview> combinationList;
    }

    public static CombinationMainPreviewList toCombinationMainPreviewList(
        List<Combination> combinations, List<CombinationImage> imgList,
        List<List<HashTagOption>> hashTagOptions) {

        System.out.println("combinations = " + hashTagOptions);

        List<CombinationMainPreview> combinationMainPreviews = combinations
            .stream()
            .map(combo -> toCombinationMain(combo, imgList, hashTagOptions))
            .collect(Collectors.toList());

        return CombinationMainPreviewList.builder()
            .combinationList(combinationMainPreviews)
            .build();
    }

    public static CombinationMainPreview toCombinationMain(Combination combination,
        List<CombinationImage> imgList, List<List<HashTagOption>> hashTagOptions) {
        // TODO: 대표 이미지 정하기

        String imageUrl = imgList.stream()
            .filter(img -> img != null && img.getCombination().getId().equals(combination.getId()))
            .findAny()  // 이미지 중 첫 번째 것만 가져옴
            .map(img -> img.getImageUrl())
            .orElse(null);  // 만약 이미지가 없다면 null 반환

        List<String> hashTagList = hashTagOptions.stream()
            .filter(htoList -> htoList.stream()
                .anyMatch(hto -> hto.getCombination().getId().equals(combination.getId())))
            .flatMap(htoList -> htoList.stream()
                .map(hto -> hto.getHashTag().getName()))
            .toList();

        return CombinationMainPreview.builder()
            .combinationId(combination.getId())
            .title(combination.getTitle())
            .combinationImageUrl(imageUrl)
            .hashTagList(hashTagList)
            .build();
    }
}

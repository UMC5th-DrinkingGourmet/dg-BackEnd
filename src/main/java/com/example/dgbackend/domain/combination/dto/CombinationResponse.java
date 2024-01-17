package com.example.dgbackend.domain.combination.dto;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combinationcomment.domain.CombinationComment;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationResponse {

    /**
     * 오늘의 조합 홈 페이지 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationPreviewDTOList {
        List<CombinationPreviewDTO> combinationList;
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
    public static class CombinationPreviewDTO {
        String title;
        String combinationImageUrl;
        Long likeCount;
        Long commentCount;
        List<String> hashTageList;
    }

    // Page<Combination> -> Page<CombinationPreviewDTO> 로 변환
    public static CombinationPreviewDTOList toCombinationPreviewDTOList(Page<Combination> combinations,
                                                                        List<List<HashTagOption>> hashTagOptions) {

        List<CombinationPreviewDTO> combinationPreviewDTOS = combinations.getContent()
                .stream()
                .map(cb -> toCombinationPreviewDTO(cb, hashTagOptions))
                .collect(Collectors.toList());

        return CombinationPreviewDTOList.builder()
                .combinationList(combinationPreviewDTOS)
                .listSize(combinationPreviewDTOS.size())
                .totalPage(combinations.getTotalPages())
                .totalElements(combinations.getTotalElements())
                .isFirst(combinations.isFirst())
                .isLast(combinations.isLast())
                .build();
    }

    // Combination -> CombinationPreviewDTO로 변환
    public static CombinationPreviewDTO toCombinationPreviewDTO(Combination combination,
                                                                List<List<HashTagOption>> hashTagOptions) {
        // TODO: 대표 이지미 정하기
        String imageUrl = combination.getCombinationImages()
                .stream()
                .findFirst()
                .map(CombinationImage::getImageUrl)
                .orElse(null);

        // 해시태그 정보
        List<String> hashTagList = hashTagOptions.stream()
                .filter(htoList -> htoList.stream()
                        .anyMatch(hto -> hto.getCombination().equals(combination)))
                .flatMap(htoList -> htoList.stream()
                        .map(hto -> hto.getHashTag().getName()))
                .toList();

        return CombinationPreviewDTO.builder()
                .title(combination.getTitle())
                .combinationImageUrl(imageUrl)
                .likeCount(combination.getLikeCount())
                .commentCount(combination.getCommentCount())
                .hashTageList(hashTagList)
                .build();
    }

    /**
     * 오늘의 조합 상세 정보 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationDetailDTO {
        CombinationResult combinationResult;
        MemberResult memberResult;
        CombinationCommentResult combinationCommentResult;
    }

    public static CombinationDetailDTO toCombinationDetailDTO(CombinationResult combinationResult,
                                                              MemberResult memberResult,
                                                              CombinationCommentResult combinationCommentResult) {
        return CombinationDetailDTO.builder()
                .combinationResult(combinationResult)
                .memberResult(memberResult)
                .combinationCommentResult(combinationCommentResult)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationResult {
        Long combinationId;
        String title;
        String content;
        List<String> hashTagList;
    }

    public static CombinationResult toCombinationResult(Combination combination,
                                                        List<HashTagOption> hashTagOptions) {
        List<String> hashTagList = hashTagOptions.stream()
                .map(hto -> hto.getHashTag().getName())
                .toList();

        return CombinationResult.builder()
                .combinationId(combination.getId())
                .title(combination.getTitle())
                .content(combination.getContent())
                .hashTagList(hashTagList)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MemberResult {
        Long memberId;
        String name;
        String profileImageUrl;
    }

    public static MemberResult toMemberResult(Member member) {
        return MemberResult.builder()
                .memberId(member.getId())
                .name(member.getName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationCommentResult {
        List<CombinationCommentPreviewDTO> combinationCommentList;
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
    public static class CombinationCommentPreviewDTO {
        Long combinationCommentId;
        String content;
        Long parentId;
    }

    public static CombinationCommentPreviewDTO toCommentPreviewDTO(CombinationComment comment) {
        return CombinationCommentPreviewDTO.builder()
                .combinationCommentId(comment.getId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .build();
    }

    public static CombinationCommentResult toCombinationCommentResult(Page<CombinationComment> comments) {

        List<CombinationCommentPreviewDTO> commentPreviewDTOS = comments.stream()
                .map(CombinationResponse::toCommentPreviewDTO)
                .toList();

        return CombinationCommentResult.builder()
                .combinationCommentList(commentPreviewDTOS)
                .listSize(commentPreviewDTOS.size())
                .totalPage(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    /**
     * 오늘의 조합 수정 정보 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationEditDTO {
        String title;
        String content;
        List<String> hashTagList;
        List<String> combinationImageUrlList;
    }

    public static CombinationEditDTO toCombinationEditDTO(Combination combination,
                                                          List<HashTagOption> hashTagOptions,
                                                          List<CombinationImage> combinationImages) {

        List<String> hashTagList = hashTagOptions.stream()
                .map(hto -> hto.getHashTag().getName())
                .toList();

        List<String> combinationImageUrlList = combinationImages.stream()
                .map(CombinationImage::getImageUrl)
                .toList();

        return CombinationEditDTO.builder()
                .title(combination.getTitle())
                .content(combination.getContent())
                .hashTagList(hashTagList)
                .combinationImageUrlList(combinationImageUrlList)
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
}

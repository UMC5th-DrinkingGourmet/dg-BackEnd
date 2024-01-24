package com.example.dgbackend.domain.recipecomment.dto;

import com.example.dgbackend.domain.recipecomment.RecipeComment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeCommentResponse {

    @NotNull
    private Long id;

    @NotNull
    private String content;

    private String MemberName;

    @Builder.Default
    private List<RecipeCommentResponse> childCommentList = new ArrayList<>();

    public static RecipeCommentResponse toResponse(RecipeComment recipeComment) {

        //부모 Comment가 null이면 0으로 설정, null이 아니면 부모 Comment의 id값으로 설정
        Long parentId = Optional.ofNullable(recipeComment.getParentComment())
                .map(RecipeComment::getId)
                .orElse(0L);

        return RecipeCommentResponse.builder()
                .id(recipeComment.getId())
                .content(recipeComment.getContent())
                .childCommentList(getList(recipeComment))
                .MemberName(recipeComment.getMember().getName())
                .build();
    }

    private static List<RecipeCommentResponse> getList(RecipeComment recipeComment) {
        return Optional.ofNullable(recipeComment.getChildCommentList())

                //자식없을 때
                .orElse(new ArrayList<>())

                //자식있을 때, 존재하는 것 만 반환
                .stream()
                .filter(RecipeComment::isState)
                .map(RecipeCommentResponse::toResponse).toList();
    }

}

package com.example.dgbackend.domain.recommend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecommendRequest {

    /*
    주류 추천 요청 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecommendRequestDTO {

        @NotNull
        private Integer desireLevel;    //취하고 싶은 정도
        @NotNull
        private String foodName;        //음식 이름
        private String feeling;         //기분
        private String weather;         //날씨
    }

    /*
    GPT API요청에 사용할 메세지 DTO
     */
    @Builder
    @Getter
    public static class GPTMessage {

        private String role;
        private String content;
    }
}

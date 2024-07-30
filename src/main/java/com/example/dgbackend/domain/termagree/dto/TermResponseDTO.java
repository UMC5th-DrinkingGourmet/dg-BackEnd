package com.example.dgbackend.domain.termagree.dto;

import com.example.dgbackend.domain.term.TermType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class TermResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAgreeResponseDTO {

        private Long memberID;
        private List<TermType> termList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermDisagreeResponseDTO {

        private Long memberID;
        private List<TermType> termList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberTermAgreeResponseDTO {

        private Long memberID;
        private List<TermType> termList;
    }
}

package com.example.dgbackend.domain.termagree.dto;

import com.example.dgbackend.domain.term.TermType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TermRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermAgreeRequestDTO {
        private List<TermType> termList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermDisagreeRequestDTO {
        private List<TermType> termList;
    }

}

package com.example.dgbackend.domain.termagree.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.termagree.dto.TermRequestDTO.TermAgreeRequestDTO;
import com.example.dgbackend.domain.termagree.dto.TermRequestDTO.TermDisagreeRequestDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.TermAgreeResponseDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.TermDisagreeResponseDTO;

public interface TermAgreeCommandService {

    TermAgreeResponseDTO termAgree(Member member, TermAgreeRequestDTO termAgreeRequestDTO);

    TermDisagreeResponseDTO termDisagree(Member member,
        TermDisagreeRequestDTO termDisagreeRequestDTO);
}

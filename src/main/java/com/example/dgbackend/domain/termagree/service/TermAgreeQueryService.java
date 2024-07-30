package com.example.dgbackend.domain.termagree.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.MemberTermAgreeResponseDTO;

public interface TermAgreeQueryService {

    MemberTermAgreeResponseDTO getMemberTermAgree(Member member);

}

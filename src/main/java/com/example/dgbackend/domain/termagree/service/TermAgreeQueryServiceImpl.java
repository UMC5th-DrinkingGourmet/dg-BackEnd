package com.example.dgbackend.domain.termagree.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.term.Term;
import com.example.dgbackend.domain.termagree.TermAgree;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.MemberTermAgreeResponseDTO;
import com.example.dgbackend.domain.termagree.repository.TermAgreeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TermAgreeQueryServiceImpl implements TermAgreeQueryService{
    private final TermAgreeRepository termAgreeRepository;

    @Override
    public MemberTermAgreeResponseDTO getMemberTermAgree(Member member) {
        List<TermAgree> termAgreeList = termAgreeRepository.findAllByMemberId(member.getId());

        return MemberTermAgreeResponseDTO.builder()
            .memberID(member.getId())
            .termList(termAgreeList.stream().map(TermAgree::getTerm).map(Term::getTermType).toList())
            .build();
    }
}

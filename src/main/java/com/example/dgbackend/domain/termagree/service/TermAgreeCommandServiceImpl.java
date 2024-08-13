package com.example.dgbackend.domain.termagree.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.term.Term;
import com.example.dgbackend.domain.term.TermType;
import com.example.dgbackend.domain.term.repository.TermRepository;
import com.example.dgbackend.domain.termagree.TermAgree;
import com.example.dgbackend.domain.termagree.dto.TermRequestDTO.TermAgreeRequestDTO;
import com.example.dgbackend.domain.termagree.dto.TermRequestDTO.TermDisagreeRequestDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.TermAgreeResponseDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.TermDisagreeResponseDTO;
import com.example.dgbackend.domain.termagree.repository.TermAgreeRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TermAgreeCommandServiceImpl implements TermAgreeCommandService {

    private final TermAgreeRepository termAgreeRepository;
    private final TermRepository termRepository;

    @Override
    public TermAgreeResponseDTO termAgree(Member member, TermAgreeRequestDTO termAgreeRequestDTO) {
        List<Term> termList = termAgreeRequestDTO.getTermList().stream()
            .map(termRepository::findByTermType).toList();

        List<Term> alreadyAgreedTermList = new ArrayList<>();
        List<TermAgree> memberTermAgreeList = termAgreeRepository.findAllByMemberId(member.getId());
        if (memberTermAgreeList != null) {
            alreadyAgreedTermList = memberTermAgreeList.stream().map(TermAgree::getTerm).toList();
        }

        List<TermType> savedList = new ArrayList<>();
        for (Term term : termList) {
            if (alreadyAgreedTermList.contains(term)) {
                continue;
            }

            savedList.add(term.getTermType());
            termAgreeRepository.save(termAgreeRepository.save(
                TermAgree.builder()
                    .term(term)
                    .member(member)
                    .build()));
        }

        return TermAgreeResponseDTO.builder()
            .memberID(member.getId())
            .termList(savedList)
            .build();
    }

    @Override
    public TermDisagreeResponseDTO termDisagree(Member member,
        TermDisagreeRequestDTO termDisagreeRequestDTO) {
        List<Term> deleteList = termDisagreeRequestDTO.getTermList().stream()
            .map(termRepository::findByTermType).toList();

        List<TermAgree> alreadyAgreedTermList = termAgreeRepository.findAllByMemberId(
            member.getId());
        if (alreadyAgreedTermList == null) {
            return TermDisagreeResponseDTO.builder()
                .memberID(member.getId())
                .termList(new ArrayList<>())
                .build();
        }

        List<TermType> savedList = new ArrayList<>();
        for (TermAgree termAgree : alreadyAgreedTermList) {
            if (!deleteList.contains(termAgree.getTerm())) {
                continue;
            }

            savedList.add(termAgree.getTerm().getTermType());
            termAgreeRepository.delete(termAgree);
        }

        return TermDisagreeResponseDTO.builder()
            .memberID(member.getId())
            .termList(savedList)
            .build();
    }

    @Override
    public void deleteCancellation(Member member) {
        termAgreeRepository.deleteAllByMember(member);
    }
}

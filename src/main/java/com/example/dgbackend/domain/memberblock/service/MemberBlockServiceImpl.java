package com.example.dgbackend.domain.memberblock.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.memberblock.MemberBlock;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockRequest.MemberBlockReq;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockResponse;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockResponse.MemberBlockResult;
import com.example.dgbackend.domain.memberblock.repository.MemberBlockRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBlockServiceImpl implements MemberBlockService {

    private final MemberBlockRepository memberBlockRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberBlockResult block(MemberBlockReq memberBlockReq, Member member) {

        Member blockedMember = memberRepository.findMemberById(memberBlockReq.getBlockedMemberId())
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));

        validateMemberBlock(blockedMember, member);

        MemberBlock prevMemberBlock = memberBlockRepository.findMemberBlockByMemberAndBlockedMember(
            member, blockedMember);

        duplicateMemberBlock(prevMemberBlock);

        MemberBlock memberBlock = MemberBlock.toEntity(blockedMember, member);

        memberBlockRepository.save(memberBlock);

        return MemberBlockResponse.toMemberBlockResult(blockedMember, member);
    }

    @Override
    public void deleteBlock(Member member) {
        memberBlockRepository.deleteAllByMember(member);
    }

    private void duplicateMemberBlock(MemberBlock prevMemberBlock) {
        if (prevMemberBlock != null) {
            throw new ApiException(ErrorStatus._DUPLICATE_MEMBER_BLOCK);
        }
    }

    private void validateMemberBlock(Member blockedMember, Member loginMember) {
        if (blockedMember.getId().equals(loginMember.getId())) {
            throw new ApiException(ErrorStatus._INVALID_MEMBER_BLOCK);
        }
    }
}

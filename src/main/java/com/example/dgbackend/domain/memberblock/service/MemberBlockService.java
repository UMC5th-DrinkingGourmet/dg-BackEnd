package com.example.dgbackend.domain.memberblock.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockRequest.MemberBlockReq;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockResponse.MemberBlockResult;

public interface MemberBlockService {

    MemberBlockResult block(MemberBlockReq memberBlockReq, Member member);

}

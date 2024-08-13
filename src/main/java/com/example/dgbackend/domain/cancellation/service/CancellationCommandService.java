package com.example.dgbackend.domain.cancellation.service;

import com.example.dgbackend.domain.member.Member;

public interface CancellationCommandService {

    Boolean postCancellation(Member member);

    void deleteCancellation(Long memberId);

    Boolean checkCancellation(Long memberId);
}

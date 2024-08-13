package com.example.dgbackend.domain.cancellation.service;

import com.example.dgbackend.domain.cancellation.Cancellation;
import com.example.dgbackend.domain.cancellation.repository.CancellationRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CancellationCommandServiceImpl implements CancellationCommandService {

    private final CancellationRepository cancellationRepository;

    @Override
    public Boolean postCancellation(Member member) {

        LocalDateTime nowTime = LocalDateTime.now();

        // 정지 시간 더하기
        nowTime = nowTime.plusDays(7);

        Cancellation cancellation = Cancellation.toEntity(nowTime, member);

        cancellationRepository.save(cancellation);

        return cancellationRepository.existsByMember(member);
    }

    @Override
    public void deleteCancellation(Long memberId) {

        Cancellation cancellation = cancellationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApiException(ErrorStatus._CANCELLATION_NOT_FOUND));

        cancellationRepository.delete(cancellation);
    }

    @Override
    public Boolean checkCancellation(Long memberId) { return cancellationRepository.existsByMemberId(memberId); }
}

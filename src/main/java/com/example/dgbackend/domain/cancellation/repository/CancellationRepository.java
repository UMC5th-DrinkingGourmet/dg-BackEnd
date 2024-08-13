package com.example.dgbackend.domain.cancellation.repository;

import com.example.dgbackend.domain.cancellation.Cancellation;
import com.example.dgbackend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CancellationRepository extends JpaRepository<Cancellation, Long>  {

    Boolean existsByMember (Member member);

    Boolean existsByMemberId (Long memberId);

    Optional<Cancellation> findByMemberId (Long memberId);

    List<Cancellation> findAllByCancelledAtBefore(LocalDateTime now);

    Optional<Cancellation> findById (Long id);
}

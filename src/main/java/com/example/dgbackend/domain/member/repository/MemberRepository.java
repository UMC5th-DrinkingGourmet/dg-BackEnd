package com.example.dgbackend.domain.member.repository;

import com.example.dgbackend.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    Boolean existsByProviderAndProviderId(String provider, String providerId);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    Optional<Member> findMemberById(Long id);

    Boolean existsByNickName(String nickName);

    void deleteById(Long id);

    @Query("SELECT m.profileImageUrl FROM Member m WHERE m.id = :memberId")
    String findImageUrlsByMember(@Param("memberId") Long memberId);
}

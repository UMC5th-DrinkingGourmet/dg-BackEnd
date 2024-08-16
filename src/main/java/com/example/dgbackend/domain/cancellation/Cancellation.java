package com.example.dgbackend.domain.cancellation;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Cancellation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime cancelledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cancellation toEntity(LocalDateTime time, Member member) {
        return Cancellation.builder()
            .cancelledAt(time)
            .member(member)
            .build();
    }
}
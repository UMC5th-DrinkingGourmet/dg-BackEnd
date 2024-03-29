package com.example.dgbackend.domain.combinationlike;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CombinationLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean state = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combination_id")
    private Combination combination;

    public CombinationLike(Combination combination, Member member) {
        this.combination = combination;
        this.member = member;
        this.combination.increaseLikeCount();
    }

    public CombinationLike changeState() {
        this.state = !this.state;
        if (!this.state) {
            this.combination.decreaseLikeCount();
        } else {
            this.combination.increaseLikeCount();
        }
        return this;
    }

    public Boolean nowCombinationLikeState() {
        return this.state;
    }
}
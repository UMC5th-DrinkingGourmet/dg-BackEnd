package com.example.dgbackend.domain.hashtagoption;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HashTagOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combination_id")
    private Combination combination;

    //== 연관관계 관련 ==//
    public void setHashTag(HashTag hashTag) {
        this.hashTag = hashTag;
    }
}

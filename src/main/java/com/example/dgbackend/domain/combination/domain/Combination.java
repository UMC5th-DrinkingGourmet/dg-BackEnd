package com.example.dgbackend.domain.combination.domain;


import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.member.domain.Member;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Combination extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @ColumnDefault("0")
    private Long likeCount;

    @ColumnDefault("0")
    private Long commentCount;

    private boolean state = true; //true : 존재, false : 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "combination", cascade = CascadeType.ALL)
    private List<CombinationImage> combinationImages = new ArrayList<>();
}

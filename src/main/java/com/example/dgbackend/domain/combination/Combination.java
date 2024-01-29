package com.example.dgbackend.domain.combination;


import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@DynamicInsert
@DynamicUpdate
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

    @Builder.Default
    private boolean state = true; //true : 존재, false : 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "combination", cascade = CascadeType.ALL)
    private List<CombinationImage> combinationImages = new ArrayList<>();

    @OneToMany(mappedBy = "combination", cascade = CascadeType.ALL)
    private List<CombinationComment> combinationComments = new ArrayList<>();


    /**
     * 연관관계 편의 메소드
     */
    public void addCombinationImage(CombinationImage combinationImage) {
        combinationImages.add(combinationImage);
        combinationImage.setCombination(this);
    }

    public void addCombinationComment(CombinationComment combinationComment) {
        combinationComments.add(combinationComment);
        commentCount += 1;
        combinationComment.setCombination(this);
    }

    /**
     * update 함수
     */
    public void updateCombination(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * CombinationLike 관련 메서드
     */
    public void increaseLikeCount() {
        this.likeCount ++;
    }
    public void decreaseLikeCount() {
        this.likeCount --;
    }
}

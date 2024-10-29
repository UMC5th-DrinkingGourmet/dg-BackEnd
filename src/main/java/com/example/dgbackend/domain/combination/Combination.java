package com.example.dgbackend.domain.combination;


import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.Recommend;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_id")
    private Recommend recommend;

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

    public void deleteCombinationComment() {
        commentCount -= 1;
    }

    /**
     * update 함수
     */
    public void updateCombination(String title, String content, Recommend recommend) {
        this.title = title;
        this.content = content;
        this.recommend = recommend;
    }

    public void delete() {
        this.state = false;
    }

    public boolean isState() {
        return state;
    }

    /**
     * CombinationLike 관련 메서드
     */
    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
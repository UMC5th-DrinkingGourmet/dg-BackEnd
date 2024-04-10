package com.example.dgbackend.domain.combinationcomment;

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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CombinationComment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private CombinationComment parentComment; //댓글 : 0, 대 댓글 : 자신의 부모 댓글 id

	@Builder.Default
	private boolean state = true; //true : 존재, false : 삭제

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "combination_id")
	private Combination combination;

	@OneToMany(mappedBy = "parentComment")
	private List<CombinationComment> childComments = new ArrayList<>();

    //== 연관관계 관련 메서드 ==//
    public void setCombination(Combination combination) {
        this.combination = combination;
    }

	public void deleteComment() {
		this.state = false;
		this.combination.deleteCombinationComment();
	}


	public void updateComment(String content) {
		this.content = content;
	}

	public void updateState() {
		this.state = false;
	}

}

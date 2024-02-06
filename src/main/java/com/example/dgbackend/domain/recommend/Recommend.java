package com.example.dgbackend.domain.recommend;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@SQLDelete(sql = "UPDATE recommend SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Recommend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer desireLevel;

    @NotNull
    private String foodName;

    private String feeling;

    private String weather;

    @NotNull
    private String drinkName;

    @NotNull
    private String drinkInfo;

    @NotNull
    private String imageUrl;

    private Boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

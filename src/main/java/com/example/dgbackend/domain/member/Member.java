package com.example.dgbackend.domain.member;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.enums.Role;
import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String birthDate; //생일은 String으로 받도록 하였습니다.

    @NotNull
    private String phoneNumber;

    @NotNull
    private String nickName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl;

    private boolean state = true; //true: 존재, false: 탈퇴

    private Integer drinkingWeeks;

    private Integer drinkingTimes;

    private String preferredAlcoholDgree; // 선호 도수

    private String limit; //주량

}

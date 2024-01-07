package com.example.dgbackend.domain.member;

import com.example.dgbackend.domain.enums.Gender;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String profileImageUrl;

    private boolean state = true; //true: 존재, false: 탈퇴

    @NotNull
    private Integer drinkingWeeks;

    @NotNull
    private Integer drinkingTimes;

}
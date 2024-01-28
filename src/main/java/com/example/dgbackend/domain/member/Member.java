package com.example.dgbackend.domain.member;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
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

    private String profileImageUrl;

    private boolean state = true; //true: 존재, false: 탈퇴

    //주류 추천 정보 입력 내용 변경됨
    private String preferredAlcoholType;  //선호 주종

    private String preferredAlcoholDegree; // 선호 도수

    private String drinkingLimit; //주량

    private String drinkingTimes; // 음주 횟수


    //주류 추천 정보 관련 setter
    /*
    Setter: preferredAlcoholType (선호 주종)
     */
    public void setPreferredAlcoholType(String alcoholType) {
        this.preferredAlcoholType = alcoholType;
    }

    /*
    Setter: preferredAlcoholDegree (선호 도수)
     */
    public void setPreferredAlcoholDegree(String alcoholDegree) {
        this.preferredAlcoholDegree = alcoholDegree;
    }

    /*
    Setter: drinkingTimes (음주 횟수)
     */
    public void setDrinkingTimes(String drinkingTimes) {
        this.drinkingTimes = drinkingTimes;
    }

    /*
    Setter: drinkingLimit (주량)
     */
    public void setDrinkingLimit(String drinkingLimit) {
        this.drinkingLimit = drinkingLimit;
    }

    public Member update (String name, String nickName, String birthDate, String phoneNumber, String profileImageUrl, Gender gender) {
        this.name = name;
        this.nickName = nickName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;

        return this;
    }

    public void signout() {
        this.state = false;
    }
}

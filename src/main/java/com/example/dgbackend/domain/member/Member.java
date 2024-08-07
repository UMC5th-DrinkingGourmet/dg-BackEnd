package com.example.dgbackend.domain.member;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.enums.Role;
import com.example.dgbackend.domain.enums.State;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
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

    private String birthDate;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String nickName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String provider;

    @NotNull
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private State state = State.TRUE; //true: 존재, false: 탈퇴

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

    /*
    Setter: name (이름)
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
    Setter: nickName (닉네임)
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /*
    Setter: birthDate (생일)
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /*
    Setter: birthDate (생일)
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /*
    Setter: gender (생일)
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return profileImageUrl;
    }

    public void setState(State state) {
        this.state = state;
    }
}

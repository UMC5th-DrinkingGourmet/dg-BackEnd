package com.example.dgbackend.domain.drinklist;

import com.example.dgbackend.domain.enums.DrinkType;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
public class DrinkList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String drinkImageUrl;

    @NotNull
    private String drinkName;

    @NotNull
    private String drinkIntro;

    @NotNull
    private String drinkContent;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;

    @NotNull
    private float alcoholPercent;

    @NotNull
    private int drinkVolume;

    @NotNull
    private LocalDate launchDate;

    @NotNull
    private String company;
}

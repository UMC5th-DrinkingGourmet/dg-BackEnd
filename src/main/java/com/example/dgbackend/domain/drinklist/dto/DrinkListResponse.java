package com.example.dgbackend.domain.drinklist.dto;

import com.example.dgbackend.domain.drinklist.DrinkList;
import com.example.dgbackend.domain.enums.DrinkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class DrinkListResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DrinkListPreview {
        Long id;
        String drinkImageUrl;
        String drinkName;
        String drinkIntro;
    }

    public static DrinkListPreview toDrinkListPreview(DrinkList drinkList) {
        return DrinkListPreview.builder()
                .id(drinkList.getId())
                .drinkImageUrl(drinkList.getDrinkImageUrl())
                .drinkName(drinkList.getDrinkName())
                .drinkIntro(drinkList.getDrinkIntro())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MainDrinkListPreviewList {
        List<DrinkListPreview> drinkPreviewList;
    }

    public static MainDrinkListPreviewList toMainDrinkListPreviewList(List<DrinkList> drinkLists) {

        List<DrinkListPreview> drinkPreviewList = drinkLists
                .stream()
                .map(drinkList -> toDrinkListPreview(drinkList))
                .collect(Collectors.toList());

        return MainDrinkListPreviewList.builder()
                .drinkPreviewList(drinkPreviewList)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DrinkListPreviewList {
        List<DrinkListPreview> drinkPreviewList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    public static DrinkListPreviewList toDrinkListPreviewList(Page<DrinkList> drinkLists) {
        List<DrinkListPreview> drinkPreviewList = drinkLists.getContent()
                .stream()
                .map(drinkList -> toDrinkListPreview(drinkList))
                .collect(Collectors.toList());

        return DrinkListPreviewList.builder()
                .drinkPreviewList(drinkPreviewList)
                .listSize(drinkPreviewList.size())
                .totalPage(drinkLists.getTotalPages())
                .totalElements(drinkLists.getTotalElements())
                .isFirst(drinkLists.isFirst())
                .isLast(drinkLists.isLast())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DetailDrinkList {
        Long id;
        String drinkImageUrl;
        String drinkName;
        String drinkIntro;
        String drinkContent;
        DrinkType drinkType;
        float alcoholPercent;
        int drinkVolume;
        LocalDate launchDate;
        String company;
    }

    public static DetailDrinkList toDetailDrinkList(DrinkList drinkList) {
        return DetailDrinkList.builder()
                .id(drinkList.getId())
                .drinkImageUrl(drinkList.getDrinkImageUrl())
                .drinkName(drinkList.getDrinkName())
                .drinkIntro(drinkList.getDrinkIntro())
                .drinkContent(drinkList.getDrinkContent())
                .drinkType(drinkList.getDrinkType())
                .alcoholPercent(drinkList.getAlcoholPercent())
                .drinkVolume(drinkList.getDrinkVolume())
                .launchDate(drinkList.getLaunchDate())
                .company(drinkList.getCompany())
                .build();
    }
}

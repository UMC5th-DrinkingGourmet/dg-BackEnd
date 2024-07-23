package com.example.dgbackend.domain.drinklist.service;

import com.example.dgbackend.domain.drinklist.dto.DrinkListResponse;
import com.example.dgbackend.domain.enums.DrinkType;

public interface DrinkListQueryService {

    DrinkListResponse.MainDrinkListPreviewList getMainDrinkListPreviewList();

    DrinkListResponse.DetailDrinkList getDetailDrinkList(Long id);

    DrinkListResponse.DrinkListPreviewList getDrinkListByDrinkType(DrinkType drinkType,
        Integer page);

    DrinkListResponse.DrinkListPreviewList getDrinkListAll(Integer page);
}

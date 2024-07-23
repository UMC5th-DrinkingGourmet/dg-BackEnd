package com.example.dgbackend.domain.drinklist.service;

import static com.example.dgbackend.domain.drinklist.dto.DrinkListResponse.DetailDrinkList;
import static com.example.dgbackend.domain.drinklist.dto.DrinkListResponse.DrinkListPreviewList;
import static com.example.dgbackend.domain.drinklist.dto.DrinkListResponse.MainDrinkListPreviewList;
import static com.example.dgbackend.domain.drinklist.dto.DrinkListResponse.toDetailDrinkList;
import static com.example.dgbackend.domain.drinklist.dto.DrinkListResponse.toDrinkListPreviewList;
import static com.example.dgbackend.domain.drinklist.dto.DrinkListResponse.toMainDrinkListPreviewList;

import com.example.dgbackend.domain.drinklist.DrinkList;
import com.example.dgbackend.domain.drinklist.repository.DrinkListRepository;
import com.example.dgbackend.domain.enums.DrinkType;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrinkListQueryServiceImpl implements DrinkListQueryService {

    private final DrinkListRepository drinkListRepository;

    @Override
    public MainDrinkListPreviewList getMainDrinkListPreviewList() {
        List<DrinkList> drinkLists = drinkListRepository.findTop4ByOrderByLaunchDateDesc();
        return toMainDrinkListPreviewList(drinkLists);
    }

    @Override
    public DetailDrinkList getDetailDrinkList(Long id) {
        DrinkList drinkList = drinkListRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_DRINK_LIST));

        return toDetailDrinkList(drinkList);
    }

    @Override
    public DrinkListPreviewList getDrinkListByDrinkType(DrinkType drinkType, Integer page) {
        Page<DrinkList> drinkLists = drinkListRepository.findAllByDrinkTypeOrderByLaunchDateDesc(
            drinkType, PageRequest.of(page, 10));

        return toDrinkListPreviewList(drinkLists);
    }

    @Override
    public DrinkListPreviewList getDrinkListAll(Integer page) {
        Page<DrinkList> drinkLists = drinkListRepository.findAllByOrderByLaunchDateDesc(
            PageRequest.of(page, 10));

        return toDrinkListPreviewList(drinkLists);
    }
}

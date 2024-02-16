package com.example.dgbackend.domain.drinklist.controller;

import com.example.dgbackend.domain.drinklist.dto.DrinkListResponse;
import com.example.dgbackend.domain.drinklist.service.DrinkListQueryService;
import com.example.dgbackend.domain.enums.DrinkType;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "새로 출시된 주류 API")
@RestController
@Validated
@RequestMapping("/drinks")
@RequiredArgsConstructor
public class DrinkListController {
    private final DrinkListQueryService drinkListQueryService;

    @Operation(summary = "메인 새로 출시된 주류 조회", description = "메인에 표시될 새로 출시된 주류 4개를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메인 새로 출시된 주류 조회 성공")
    })
    @GetMapping("/main")
    public ApiResponse<DrinkListResponse.MainDrinkListPreviewList> getMainDrinkLists() {

        DrinkListResponse.MainDrinkListPreviewList mainDrinkListPreviewList =
                drinkListQueryService.getMainDrinkListPreviewList();

        return ApiResponse.onSuccess(mainDrinkListPreviewList);
    }

    @Operation(summary = "새로 출시된 주류 상세 조회", description = "새로 출시된 주류 하나를 상세 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "새로 출시된 주류 상세 조회 성공")
    })
    @Parameter(name = "drinkListId", description = "새로 출시된 주류 ID", required = true)
    @GetMapping("/{drinkListId}")
    public ApiResponse<DrinkListResponse.DetailDrinkList> getDetailDrinkList(
            @PathVariable(name = "drinkListId") Long drinkListId) {

        DrinkListResponse.DetailDrinkList detailDrinkList =
                drinkListQueryService.getDetailDrinkList(drinkListId);

        return ApiResponse.onSuccess(detailDrinkList);
    }

    @Operation(summary = "주류 종류별 조회", description = "주류 종류별로 새로 출시된 주류를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주류 종류별 조회 성공")
    })
    @Parameter(name = "drinkType", description = "주류 종류", required = true)
    @GetMapping("/type")
    public ApiResponse<DrinkListResponse.DrinkListPreviewList> getDrinkListByDrinkType(
            @RequestParam(name = "drinkType") DrinkType drinkType,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        DrinkListResponse.DrinkListPreviewList drinkListPreviewList =
                drinkListQueryService.getDrinkListByDrinkType(drinkType, page);

        return ApiResponse.onSuccess(drinkListPreviewList);
    }

    @Operation(summary = "주류 전체 리스트 조회", description = "새로 출시된 주류 리스트를 전체 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주류 리스트 조회 성공")
    })
    @Parameter(name = "drinkType", description = "주류 종류", required = true)
    @GetMapping("/all")
    public ApiResponse<DrinkListResponse.DrinkListPreviewList> getDrinkListAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        DrinkListResponse.DrinkListPreviewList drinkListPreviewList =
                drinkListQueryService.getDrinkListAll(page);

        return ApiResponse.onSuccess(drinkListPreviewList);
    }
}

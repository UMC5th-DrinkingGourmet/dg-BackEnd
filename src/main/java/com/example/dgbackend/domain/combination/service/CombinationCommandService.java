package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationRequest.WriteCombination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.member.Member;

public interface CombinationCommandService {

    CombinationResponse.CombinationProcResult uploadCombination(WriteCombination request,
        Member loginMember);

    CombinationResponse.CombinationProcResult deleteCombination(Long combinationId);

    CombinationResponse.CombinationProcResult editCombination(Long combinationId,
        WriteCombination request);

    void deleteAllCombination(Long memberId);

    void deleteCombinationWithRelations(Combination combination);
}

package com.example.dgbackend.domain.hashtagoption.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.domain.hashtag.repository.HashTagRepository;
import com.example.dgbackend.domain.hashtag.service.HashTagQueryService;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagOptionCommandServiceImpl implements HashTagOptionCommandService {

    private final HashTagOptionRepository hashTagOptionRepository;
    private final HashTagQueryService hashTagQueryService;
    private final HashTagRepository hashTagRepository;

    @Override
    public void deleteAllHashTagOption(Long combinationId) {
        hashTagOptionRepository.deleteAllByCombinationId(combinationId);
    }

    @Override
    public void updateHashTagOption(Combination combination, List<String> hashTagNames) {

        // 0. 기존에 작성된 HashTagOption 정보 조회
        List<HashTagOption> existHashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(
            combination);

        // 1. 기존에 작성된 해시태그를 취소한 경우 HashTagOption 삭제
        removeCancelledHashTagOption(combination, hashTagNames, existHashTagOptions);

        // 2. 새로 추가된 해시태그 처리 (DB에 있는 경우 & 새로 추가하는 경우)
        processNewHashTags(combination, hashTagNames, existHashTagOptions);
    }

    private void removeCancelledHashTagOption(Combination combination,
        List<String> requestHashTagNames, List<HashTagOption> existHashTagOptions) {

        // 수정하면서 지워진 HashTag 파싱
        List<String> delNameToHashTagOption = existHashTagOptions.stream()
            .map(hto -> hto.getHashTag().getName())
            .filter(name -> !requestHashTagNames.contains(name))
            .toList();

        for (String delHashName : delNameToHashTagOption) {
            HashTagOption findHashTagOption = hashTagOptionRepository.findByCombinationAndHashTagName(
                combination, delHashName);
            hashTagOptionRepository.deleteById(findHashTagOption.getId());
        }
    }

    private void processNewHashTags(Combination combination, List<String> requestHashTagNames,
        List<HashTagOption> existHashTagOptions) {

        // 추가된 HashTag name 파싱하기
        List<String> newHashTagNames = requestHashTagNames.stream()
            .filter(request -> existHashTagOptions.stream()
                .noneMatch(exist -> request.equals(exist.getHashTag().getName())))
            .toList();

        /**
         * 추가된 HashTag가 DB에 있는지 확인하기
         * 1. 있는 경우, HashTag 조회 후 사용
         * 2. 없는 경우, 새로운 HashTag 생성 후 사용
         */
        for (String newHashTagName : newHashTagNames) {
            Optional<HashTag> optionalHashTag = hashTagQueryService.findHashTagByName(
                newHashTagName);
            if (optionalHashTag.isPresent()) {
                HashTagOption newHashTagOption = createHashTagOption(combination,
                    optionalHashTag.get());
                hashTagOptionRepository.save(newHashTagOption);
            } else {
                HashTag newHashTag = createHashTag(newHashTagName);
                HashTagOption newHashTagOption = createHashTagOption(combination, newHashTag);

                hashTagRepository.save(newHashTag);
                hashTagOptionRepository.save(newHashTagOption);
            }
        }
    }

    private HashTag createHashTag(String hashTagName) {
        return HashTag.builder()
            .name(hashTagName)
            .build();
    }

    private HashTagOption createHashTagOption(Combination combination, HashTag hashTag) {
        return HashTagOption.builder()
            .combination(combination)
            .hashTag(hashTag)
            .build();
    }
}

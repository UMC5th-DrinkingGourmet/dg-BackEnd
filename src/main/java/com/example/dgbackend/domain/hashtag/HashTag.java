package com.example.dgbackend.domain.hashtag;

import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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
public class HashTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "hashTag", cascade = CascadeType.ALL)
    List<HashTagOption> hashTagOptionList = new ArrayList<>();

    /**
     * 연관관계 편의 메소드
     */
    public void addHashTagOption(HashTagOption hashTagOption) {
        hashTagOptionList.add(hashTagOption);
        if (hashTagOption.getHashTag() != this) {
            hashTagOption.setHashTag(this);
        }
    }
}

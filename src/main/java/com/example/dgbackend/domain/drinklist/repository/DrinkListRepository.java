package com.example.dgbackend.domain.drinklist.repository;

import com.example.dgbackend.domain.drinklist.DrinkList;
import com.example.dgbackend.domain.enums.DrinkType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkListRepository extends JpaRepository<DrinkList, Long> {

    List<DrinkList> findTop4ByOrderByLaunchDateDesc();

    Optional<DrinkList> findById(Long id);

    Page<DrinkList> findAllByDrinkTypeOrderByLaunchDateDesc(DrinkType drinkType,
        PageRequest pageRequest);

    Page<DrinkList> findAllByOrderByLaunchDateDesc(PageRequest pageRequest);
}

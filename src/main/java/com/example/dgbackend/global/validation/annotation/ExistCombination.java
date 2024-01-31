package com.example.dgbackend.global.validation.annotation;

import com.example.dgbackend.global.validation.validator.CombinationExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CombinationExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistCombination {

    String message() default "존재하지 않는 오늘의 조합 게시글 입니다..";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

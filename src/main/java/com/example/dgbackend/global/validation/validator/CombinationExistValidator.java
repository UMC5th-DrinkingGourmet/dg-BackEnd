package com.example.dgbackend.global.validation.validator;

import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.validation.annotation.ExistCombination;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CombinationExistValidator implements ConstraintValidator<ExistCombination, Long> {

    private final CombinationQueryService combinationQueryService;

    @Override
    public void initialize(ExistCombination constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long combinationId, ConstraintValidatorContext context) {

        boolean valid = combinationQueryService.existCombination(combinationId);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus._COMBINATION_NOT_FOUND.toString()).addConstraintViolation();
        }
        return valid;
    }

}

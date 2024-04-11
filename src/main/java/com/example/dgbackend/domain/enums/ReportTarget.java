package com.example.dgbackend.domain.enums;

import lombok.Getter;

public enum ReportTarget {

	COMBINATION("조합글"),
	RECIPE("레시피글"),
	COMBINATION_COMMENT("조합 댓글"),
	RECIPE_COMMENT("레시피 댓글");

	@Getter
	private final String name;

	ReportTarget(String name) {
		this.name = name;
	}

}

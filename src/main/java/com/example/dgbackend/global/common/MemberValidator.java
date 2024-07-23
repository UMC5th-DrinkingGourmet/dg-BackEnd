package com.example.dgbackend.global.common;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;

public class MemberValidator {

    //현재 viewer와 작성자가 같지 않으면 예외 처리
    public static void match(Member viewer, Member writer) {
        if ((viewer == null) || !(viewer.getId().equals(writer.getId()))) {
            throw new ApiException(ErrorStatus._INVALID_MEMBER);
        }
    }

    //현재 viewer와 작성자가 같으면 수정/삭제 권한 부여(true 리턴)
    public static boolean isMatch(Member viewer, Member writer) {
        return (viewer != null) && (viewer.getId().equals(writer.getId()));
    }
}

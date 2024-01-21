package com.example.dgbackend.domain.member.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{
}

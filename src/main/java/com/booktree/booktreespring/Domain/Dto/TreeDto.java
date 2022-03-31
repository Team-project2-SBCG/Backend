package com.booktree.booktreespring.Domain.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TreeDto {
    private String title;       // 제목
    private String explanation; // 상세 설명
}

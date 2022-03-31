package com.booktree.booktreespring.Domain.Dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeDto {
    private String title;       // 제목
    private String explanation; // 상세 설명
}

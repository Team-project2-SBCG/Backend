package com.booktree.booktreespring.Domain.Dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeUpdateDto {
    private String title;       // 제목
    private String explanation; // 상세 설명
    private String ownerName;   // 소유자 이름
}

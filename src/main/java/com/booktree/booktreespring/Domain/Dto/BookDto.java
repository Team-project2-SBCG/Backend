package com.booktree.booktreespring.Domain.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class BookDto {
    private String ownerName;   // 트리 소유자 이름
    private String title;       // 책 제목
    private String content;     // 후기 및 설명
    private int score;          // 평점
    private MultipartFile file; // 책 이미지
}

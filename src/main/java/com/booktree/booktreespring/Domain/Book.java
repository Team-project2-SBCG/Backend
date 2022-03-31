package com.booktree.booktreespring.Domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    private String ownerName;   // 트리 소유자 이름
    private String title;       // 책 제목
    private String content;     // 후기 및 설명
    private int score;          // 평점
    private String filename;    // 사진 파일 이름
}

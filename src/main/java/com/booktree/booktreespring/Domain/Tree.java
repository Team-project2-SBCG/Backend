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
@Table(name = "tree")
public class Tree {
    @Id
    @GeneratedValue
    private Long id;            // 트리 id

    private String userName;    // 작성자 이름
    private String title;       // 트리 제목
    private String explanation; // 상세 설명
    private int upCnt;          // 좋아요 개수
}

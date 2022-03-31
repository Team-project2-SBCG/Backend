package com.booktree.booktreespring.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "user")
public class User {
    @Id @GeneratedValue
    private Long userId;

    private String userName;
    private String userPwd;
}

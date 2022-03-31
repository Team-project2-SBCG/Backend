package com.booktree.booktreespring.Domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id @GeneratedValue @Column(name ="user_id")
    private Long userId;

    @Column(name = "usernamd", length = 50, unique = true)
    private String userName;
    @Column(name = "password", length = 100)
    private String userPwd;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}

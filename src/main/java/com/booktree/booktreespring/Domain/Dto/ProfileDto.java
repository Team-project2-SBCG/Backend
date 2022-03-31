package com.booktree.booktreespring.Domain.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileDto {
    private Long id;
    private String username;
}

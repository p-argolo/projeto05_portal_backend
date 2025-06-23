package com.BaneseLabes.portal.dto;

import com.BaneseLabes.portal.model.user.RoleName;

import java.util.List;

public record CreateUserDto(

        String email,
        String username,
        String password,
        String cnpj,
        String companyName,
        List<RoleName> role

) {
}
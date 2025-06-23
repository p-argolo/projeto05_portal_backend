package com.BaneseLabes.portal.model.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Role {
    private String id;

    private RoleName name;
}

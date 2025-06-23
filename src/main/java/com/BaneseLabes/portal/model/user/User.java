package com.BaneseLabes.portal.model.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "user")
public class User {
    @Id
    private  String id;
    private String password;
    private String email;
    private String username;
    private String companyName;
    private String cnpj;
    private List<Role> roles;
}
package com.example.demo.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("Пользователь"),
    ADMIN("Администратор"),
    SALER("Сотрудник");

    public String getRoleName() {
        return roleName;
    }

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority(){
        return name();
    }
}

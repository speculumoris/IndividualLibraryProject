package com.lib.domain.enums;

public enum RoleType {

    ROLE_MEMBER("Member"),
    ROLE_EMPLOYEE("Employee"),
    ROLE_ADMIN("Administrator");

    private String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

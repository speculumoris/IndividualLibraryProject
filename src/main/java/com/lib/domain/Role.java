package com.lib.domain;

import com.lib.domain.enums.RoleType;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "t_role")
public class Role {

    @Id
    @ GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    @Override
    public String toString() {
        return "Role{" +
                " roleType=" + roleType +
                '}';
    }
}

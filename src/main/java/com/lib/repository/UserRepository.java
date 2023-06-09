package com.lib.repository;

import com.lib.domain.Role;
import com.lib.domain.User;
import com.lib.domain.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Boolean existsByEmail(String email);//unique
    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserById(Long id);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = "roles") // 2 query
    Page<User> findAll(Pageable pageable);



    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    @Modifying
    @Query("UPDATE User u SET u.firstName=:firstName,u.lastName=:lastName,u.address=:address,u.phone=:phone,u.birthDate=:birthDate,u.email=:email,u.resetPasswordCode=:resetPasswordCode WHERE u.id=:id")
    void update(@Param("id") Long id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("address")String address,
                @Param("phone") String phone,
                @Param("birthDate") String birthDate,
                @Param("email") String email,
                @Param("resetPasswordCode") String resetPasswordCode);
}

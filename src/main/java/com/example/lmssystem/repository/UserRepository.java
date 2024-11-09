package com.example.lmssystem.repository;

import com.example.lmssystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Modifying
    @Query("update User set locale=:lang where id=:id")
    void updateLocale(Long id, String lang);

    @Query("update User set password=:password,username=:username,passwordSize=:passwordSize where id=:id")
    void set(String password,String username,Integer passwordSize,Long id);

    @Query("select u from User u where u.username=:username and u.deleted=false")
    Optional<User> findByUsernameAndDeletedFalse(String username);

    List<User> findAll();
    Optional<User> findById(Long id);
    @Modifying
    @Query("UPDATE User u SET u.deleted = true WHERE u.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE "
            + "(:id IS NULL OR u.id = :id) AND "
            + "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND "
            + "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND "
            + "(:phoneNumber IS NULL OR u.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))")
    List<User> searchEmployees(@Param("id") Long id,
                               @Param("firstName") String firstName,
                               @Param("lastName") String lastName,
                               @Param("phoneNumber") String phoneNumber);

    List<User> findByDeletedTrue();

    List<User> findByRole_Name(String roleName);

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name NOT IN :roleNames")
    List<User> findByRole_NameNotIn(@Param("roleNames") List<String> roleNames);
}
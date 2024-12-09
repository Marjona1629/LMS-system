package com.example.lmssystem.repository;

import com.example.lmssystem.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllNonDeleted();

    @Modifying
    @Query("update User set locale=:lang where id=:id")
    void updateLocale(Long id, String lang);

    @Query("select u from User u where u.username=:username and u.deleted=false")
    Optional<User> findByUsernameAndDeletedFalse(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.deleted = true WHERE u.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u WHERE "
            + "(:id IS NULL OR u.id = :id) "
            + "AND (:firstName IS NULL OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :firstName, '%'))) "
            + "AND (:lastName IS NULL OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :lastName, '%'))) "
            + "AND (:phoneNumber IS NULL OR u.phone_number LIKE CONCAT('%', :phoneNumber, '%'))", nativeQuery = true)
    List<User> searchEmployees(@Param("id") Long id,
                               @Param("firstName") String firstName,
                               @Param("lastName") String lastName,
                               @Param("phoneNumber") String phoneNumber);

    List<User> findByDeletedTrue();

    List<User> findByRole_Name(String roleName);

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name NOT IN :roleNames")
    List<User> findByRole_NameNotIn(@Param("roleNames") List<String> roleNames);
}
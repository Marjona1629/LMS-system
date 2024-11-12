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

    @Query("select u from User u where u.username=:username and u.deleted=false")
    Optional<User> findByUsernameAndDeletedFalse(String username);

    @Modifying
    @Query("UPDATE User u SET u.deleted = true WHERE u.id = :id")
    void softDeleteById(@Param("id") Long id);
}
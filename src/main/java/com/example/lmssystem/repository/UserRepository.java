package com.example.lmssystem.repository;

import com.example.lmssystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Modifying
    @Query("update User set locale=:lang where id=:id")
    void updateLocale(Long id, String lang);

    @Query("update User set password=:password,username=:username,passwordSize=:passwordSize where id=:id")
    void set(String password,String username,String passwordSize,Long id);

    @Query("select u from User u where u.username=:username and u.deleted=false")
    Optional<User> findByUsernameAndDeletedFalse(String username);
}

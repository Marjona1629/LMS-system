package com.example.lmssystem.repository;

import com.example.lmssystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
    @Query("update User u set u.deleted=true where u.id=:id")
    void softDeleteById(Long id);
}
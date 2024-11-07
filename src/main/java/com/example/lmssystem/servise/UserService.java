package com.example.lmssystem.servise;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllEmployees() {
        return userRepository.findAll();
    }

    public Optional<User> getEmployeeById(Long id) {
        return userRepository.findById(id);
    }

    public User addEmployee(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateEmployee(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setPhoneNumber(user.getPhoneNumber());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setRole(user.getRole());
            updatedUser.setBirthDate(user.getBirthDate());
            updatedUser.setGender(user.getGender());
            updatedUser.setBranches(user.getBranches());
            return Optional.of(userRepository.save(updatedUser));
        }
        return Optional.empty();
    }

    public boolean softDeleteEmployee(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.softDeleteById(id);
            return true;
        }
        return false;
    }
}
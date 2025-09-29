package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.dto.UserDto;
import com.example.LibraryManagementSystem.model.entity.UserEntity;
import com.example.LibraryManagementSystem.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;


    public UserEntity register(UserDto userDto) {
        if (userRepo.findByUsername(userDto.getUsername()) != null) {
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }
        if (userDto.getEmail() != null && userRepo.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }

        UserEntity user = new UserEntity(userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getAdmin());

        return userRepo.save(user);
    }

    public UserEntity login(String username, String password) {
        UserEntity userEntity = userRepo.findByUsername(username);
        if (userEntity != null) {
            if (userEntity.getPassword().equals(password)) {
                return userEntity;
            }
        }
        return null;
    }

}

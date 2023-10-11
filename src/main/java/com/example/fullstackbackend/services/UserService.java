package com.example.fullstackbackend.services;

import com.example.fullstackbackend.model.User;
import com.example.fullstackbackend.entities.UserEntity;
import com.example.fullstackbackend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserById(Integer id) {
        return userRepository.getUserEntityById(id);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> insertNewUser(HttpEntity<String> user) {
        Optional<UserEntity> insertedUser = Optional.empty();

        //JSON input from body is turned into a user model
        Optional<User> userFromHttpBody = jsonToUserModel(user.getBody());

        //Check if the data is valid
        if (userFromHttpBody.isPresent()) {
            UserEntity newUser = userEntityMapper(userFromHttpBody.get());
            Optional<UserEntity> oldUser = Optional.ofNullable(userRepository.getUserByEmail(newUser.getEmail()));
            if (oldUser.isEmpty()) {
                UserEntity returnedUser = userRepository.save(newUser);
                insertedUser = Optional.of(returnedUser);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email already exists!");
            }
        }
        return insertedUser;
    }

    private UserEntity userEntityMapper(User user) {
        return new UserEntity(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                new HashSet<>()
        );
    }

    private Optional<User> jsonToUserModel(String jsonUser) {
        ObjectMapper mapper = new ObjectMapper();
        Optional<User> user = Optional.empty();

        try {
            User mappedUser = mapper.readValue(jsonUser, User.class);
            user = Optional.of(mappedUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Optional<UserEntity> updateUser(Integer id, HttpEntity<String> user) {
        Optional<UserEntity> updatedUser = Optional.empty();
        Optional<UserEntity> oldUser = Optional.ofNullable(userRepository.getUserEntityById(id));

        if (oldUser.isEmpty()) {
            return updatedUser;
        }

        Optional<User> userFromHttpBody = jsonToUserModel(user.getBody());

        if (userFromHttpBody.isPresent()) {
            UserEntity userToBeUpdated = updatedUser(userFromHttpBody.get(), oldUser.get());
            UserEntity returnedUser = userRepository.save(userToBeUpdated);
            updatedUser = Optional.of(returnedUser);
        }
        return updatedUser;
    }

    private UserEntity updatedUser(User newUserInformation, UserEntity user) {
        if (newUserInformation.getName() != null){
            user.setName(newUserInformation.getName());
        }
        if (newUserInformation.getEmail() != null){
            user.setEmail(newUserInformation.getEmail());
        }
        if (newUserInformation.getPassword() != null){
            user.setPassword(newUserInformation.getPassword());
        }

        return user;
    }

    public Optional<UserEntity> deleteUser(Integer id) {
        Optional<UserEntity> userToBeDeleted = userRepository.findById(id);

        if (userToBeDeleted.isPresent()) {
            userRepository.deleteById(id);
        }
        return  userToBeDeleted;
    }
}

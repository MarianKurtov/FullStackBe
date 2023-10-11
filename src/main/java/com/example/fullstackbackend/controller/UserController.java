package com.example.fullstackbackend.controller;


import com.example.fullstackbackend.entities.UserEntity;
import com.example.fullstackbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public UserEntity getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    public List<UserEntity> getUserById() {
        return userService.getAllUsers();
    }

    @PostMapping("create-user")
    public ResponseEntity<Integer> addUser(HttpEntity<String> httpEntity) {
        //return ResponseEntity<Integer> because we want to return userId when the user is saved.

        Optional<UserEntity> insertionSuccess = userService.insertNewUser(httpEntity);

        //Set default null and CONFLICT
        Integer userId = null;
        HttpStatus status = HttpStatus.CONFLICT;

        //If there is user, so insert it at the DB. Then return user id and status OK
        if (insertionSuccess.isPresent()) {
            userId = Math.toIntExact(insertionSuccess.get().getId());
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(userId, status);
    }

    @PutMapping("update-user")
    public ResponseEntity<Integer> updateUser(@RequestParam Integer id, HttpEntity<String> httpEntity) {
        Optional<UserEntity> insertionSuccess = userService.updateUser(id, httpEntity);
        Integer userId = null;
        HttpStatus status = HttpStatus.CONFLICT;

        if (insertionSuccess.isPresent()) {
            userId = id;
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(userId, status);
    }

    @DeleteMapping("delete-user")
    public ResponseEntity<Integer> deleteUser(@RequestParam Integer id) {

         Optional<UserEntity> deleteSuccess = userService.deleteUser(id);

        Integer userId = null;
        HttpStatus status = HttpStatus.CONFLICT;

        if (deleteSuccess.isPresent()) {
            userId = id;
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(userId, status);
    }
}

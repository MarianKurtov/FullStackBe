package com.example.fullstackbackend.repository;

import com.example.fullstackbackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// UserEntity - The connection to which table we want to connect
// Integer    - The type of primary Key
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity getUserEntityById(Integer id);

    Optional<UserEntity> getUserEntityByPassword(String pass);

    UserEntity getUserByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}

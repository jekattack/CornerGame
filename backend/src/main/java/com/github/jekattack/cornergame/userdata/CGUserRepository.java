package com.github.jekattack.cornergame.userdata;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CGUserRepository extends MongoRepository<CGUser, String> {

    Optional<CGUser> findByUsername(String username);
    Optional<CGUser> findByEmail(String email);

}

package com.BaneseLabes.portal.repository;

import java.util.Optional;

import com.BaneseLabes.portal.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}

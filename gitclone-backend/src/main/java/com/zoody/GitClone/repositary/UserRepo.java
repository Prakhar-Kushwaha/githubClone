package com.zoody.GitClone.repositary;

import com.zoody.GitClone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User , String> {
    Optional<User> findById(String id);
}

package com.github.simonthecat.spring.security.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    @Query("{ 'username' : ?0 }")
    UserAccount findOneByUsername(String username);
}

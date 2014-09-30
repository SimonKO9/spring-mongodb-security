package com.github.simonthecat.spring.security.mongo;

import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

public class MongoUserService {

    private final UserAccountRepository repo;

    public MongoUserService(final MongoRepositoryFactory factory) {
        this.repo = factory.getRepository(UserAccountRepository.class);
    }

    public UserAccount getUserByUsername(String username) {
        return repo.findOneByUsername(username);
    }
}

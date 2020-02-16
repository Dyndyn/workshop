package com.dyndyn.workshop.persist.repo;

import com.dyndyn.workshop.persist.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String userName);
    Collection<User> findAll();
    User create(User user);
    User delete(Integer id);

}

package com.dyndyn.workshop.persist.repo.impl;

import com.dyndyn.workshop.ValidationException;
import com.dyndyn.workshop.persist.model.User;
import com.dyndyn.workshop.persist.repo.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Set<String> emails = new HashSet<>();
    private final Set<String> usernames = new HashSet<>();
    private final AtomicInteger lastId = new AtomicInteger();

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public synchronized User create(User user) {
        Objects.requireNonNull(user);
        if (emails.contains(user.getEmail())) {
            throw new ValidationException("Such email already exists");
        }
        if (usernames.contains(user.getUsername())) {
            throw new ValidationException("Such username already exists");
        }
        user.setId(lastId.incrementAndGet());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        usernames.add(user.getUsername());
        return user;
    }

    @Override
    public synchronized User delete(Integer id) {
        User user = users.remove(id);
        if (user != null) {
            emails.remove(user.getEmail());
            usernames.remove(user.getUsername());
        }
        return user;
    }
}

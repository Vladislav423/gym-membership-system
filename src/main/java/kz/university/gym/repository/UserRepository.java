package kz.university.gym.repository;

import kz.university.gym.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}

package ru.otus;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);
}

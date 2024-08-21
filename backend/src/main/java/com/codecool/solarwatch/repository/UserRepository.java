package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.String.format;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    /*private final ConcurrentMap<String, UserEntity> users = new ConcurrentHashMap<>();

    public synchronized Optional<UserEntity> findUserByName(String userName) {
        return Optional.ofNullable(users.get(userName));
    }

    public synchronized void createUser(UserEntity user) {
        String userName = user.getUsername();
        if (users.containsKey(userName)) {
            throw new IllegalArgumentException(format("user %s already exists", userName));
        }
        users.put(userName, user);
    }

    public void updateUser(UserEntity userEntity) {
    }*/
}

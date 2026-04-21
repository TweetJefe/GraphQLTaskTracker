package com.tasktrackergraphql.user.service;

import com.tasktrackergraphql.exceptions.NullableViolation;
import com.tasktrackergraphql.exceptions.ServerException;
import com.tasktrackergraphql.exceptions.UniquenessViolation;
import com.tasktrackergraphql.user.dto.UserResponse;
import com.tasktrackergraphql.user.mapper.UserMapper;
import com.tasktrackergraphql.user.model.UserEntity;
import com.tasktrackergraphql.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";

    @Transactional
    @Override
    public UserEntity loginOrRegister(Long telegramId, String username, String languageCode) {
        log.info("Starting loginOrRegister for telegramId: {}, username: {}", telegramId, username);
        return repository.findByTelegramId(telegramId)
                .map(user -> {
                    log.info("User found in database: ID = {}", user.getId());
                    user.setLanguageCode(languageCode);
                    return user;
                })
                .orElseGet(() -> {
                    log.info("User not found. Creating a new one...");
                    UserEntity newUser = new UserEntity();
                    newUser.setTelegramId(telegramId);
                    newUser.setUsername(username);
                    newUser.setName("name");
                    newUser.setLanguageCode(languageCode);

                    UserEntity savedUser = saveUser(newUser);
                    log.info("Successfully created new user with ID: {}", savedUser.getId());
                    return savedUser;
                });
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        try {
            log.info("Saving user to repository: {}", user.getUsername());
            return repository.save(user);
        } catch (DataIntegrityViolationException exception) {
            log.error("Data integrity violation while saving user: {}", exception.getMessage());
            Throwable cause = exception.getCause();
            if (cause instanceof ConstraintViolationException cve) {
                String sqlState = cve.getSQLState();
                log.error("SQL State: {}, Constraint Name: {}", sqlState, cve.getConstraintName());
                if (sqlState.equals(PostgreSQLUniquenessViolation)) {
                    throw new UniquenessViolation(cve.getConstraintName());
                } else if (sqlState.equals(PostgreSQLNullableViolation)) {
                    throw new NullableViolation("Field cannot be null");
                }
            }
            throw new ServerException("Database error occurred: " + exception.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during user save: ", e);
            throw e;
        }
    }

    @Override
    public Long deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        repository.findById(userId)
                .ifPresentOrElse(user -> {
                            repository.deleteById(userId);
                            log.info("User deleted successfully");
                        },
                        () -> {
                            log.warn("Attempted to delete non-existent user with ID: {}", userId);
                            throw new EntityNotFoundException("User not found!");
                        }
                );
        return userId;
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        UserEntity user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID: {} not found", id);
                    return new EntityNotFoundException();
                });
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UserEntity entity = repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return mapper.toResponse(entity);
    }
}

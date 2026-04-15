package com.tasktrackergraphql.user.service;

import com.tasktrackergraphql.user.dto.UserResponse;
import com.tasktrackergraphql.user.model.UserEntity;
import com.tasktrackergraphql.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
    UserEntity loginOrRegister (Long telegramId, String username);

    UserEntity saveUser(UserEntity task);

    Long deleteUser(Long userId);

    UserResponse getUserById(Long id);
}

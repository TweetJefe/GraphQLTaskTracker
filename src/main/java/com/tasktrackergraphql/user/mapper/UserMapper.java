package com.tasktrackergraphql.user.mapper;

import com.tasktrackergraphql.user.dto.UserResponse;
import com.tasktrackergraphql.user.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse (UserEntity user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getTelegramId(),
                user.getUsername(),
                user.getName(),
                user.getLanguageCode(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


    public UserEntity toEntity (UserResponse response){
        if (response == null) return null;

        UserEntity user = new UserEntity();
        user.setName(response.name());
        user.setUsername(response.username());
        user.setTelegramId(response.telegramId());
        user.setCreatedAt(response.createdAt());
        user.setUpdatedAt(response.updatedAt());

        return user;
    }
}

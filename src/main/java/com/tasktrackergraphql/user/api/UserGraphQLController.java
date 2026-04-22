package com.tasktrackergraphql.user.api;

import com.tasktrackergraphql.TaskTrackerFacade;
import com.tasktrackergraphql.config.AuthResponse;
import com.tasktrackergraphql.config.JWTService;
import com.tasktrackergraphql.config.TelegramAuthService;
import com.tasktrackergraphql.user.dto.UserResponse;
import com.tasktrackergraphql.user.model.UserEntity;
import com.tasktrackergraphql.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final TelegramAuthService telegramAuthService;
    private final UserService userService;
    private final JWTService jwtService;
    private final TaskTrackerFacade facade;

    @MutationMapping
    public AuthResponse authViaTelegram(
            @Argument String initData,
            @Argument Long telegramId,
            @Argument String username,
            @Argument String languageCode
    ) {
        UserEntity user = userService.loginOrRegister(telegramId, username, languageCode);
        String token = jwtService.generateToken(user.getId());
        return new AuthResponse(token, user.getId());
    }

    @MutationMapping
    public Long deleteUser(
            @Argument Long userId
    ){
        return facade.deleteUser(userId);
    }


    @QueryMapping
    public UserResponse getUserById(
            @Argument Long userId
    ){
        return userService.getUserById(userId);
    }


}
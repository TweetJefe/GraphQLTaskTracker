package com.tasktrackergraphql.user.repository;

import com.tasktrackergraphql.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

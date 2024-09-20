package com.attendance.management.repo;



import com.attendance.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUniqueId(String uniqueId);  // Find user by uniqueId
    boolean existsByUniqueId(String uniqueId);  // Check if uniqueId already exists
}
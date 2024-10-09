package com.attendance.management.service;



import com.attendance.management.entity.Role;
import com.attendance.management.entity.User;
import com.attendance.management.repo.RoleRepository;
import com.attendance.management.repo.UserRepository;
import com.attendance.management.util.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user with the role
    public User registerUser(User user, String roleName) {
        Role role = roleRepository.findByName (roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Generate a unique 8-digit ID
        String uniqueId;
        do {
            uniqueId = UniqueIdGenerator.generateUniqueId();
        } while (userRepository.existsByUniqueId(uniqueId));

        // Set the role and encode the password
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUniqueId(uniqueId);  // Set the unique 8-digit ID

        // Validate phone number
        if (user.getPhoneNumber().length() != 10 || !user.getPhoneNumber().matches("\\d+")) {
            throw new RuntimeException("Phone number must be 10 digits and contain only numbers.");
        }

        // Save the user
        return userRepository.save(user);
    }
}

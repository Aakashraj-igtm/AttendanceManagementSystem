package com.attendance.management.config;

import com.attendance.management.entity.Role;
import com.attendance.management.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("Admin").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("Admin");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("Professor").isEmpty()) {
            Role professorRole = new Role();
            professorRole.setName("Professor");
            roleRepository.save(professorRole);
        }

        if (roleRepository.findByName("Student").isEmpty()) {
            Role studentRole = new Role();
            studentRole.setName("Student");
            roleRepository.save(studentRole);
        }
    }
}

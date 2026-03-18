package com.example.demo.service;

import com.example.demo.entity.AdminModel;
import com.example.demo.repository.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminModel admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
        
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole())
                .accountLocked(!admin.getIsActive())
                .build();
    }

    /**
     * Create a new admin user with encoded password.
     */
    public AdminModel createAdmin(AdminModel admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Admin with email " + admin.getEmail() + " already exists");
        }
        // Encode password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    /**
     * Get admin by email.
     */
    public Optional<AdminModel> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    /**
     * Check if admin exists by email.
     */
    public boolean adminExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    /**
     * Update last login timestamp for admin.
     */
    public void updateLastLogin(String email) {
        Optional<AdminModel> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            AdminModel admin = adminOpt.get();
            admin.setLastLogin(LocalDateTime.now());
            adminRepository.save(admin);
        }
    }

    /**
     * Get admin by ID.
     */
    public Optional<AdminModel> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * Update admin.
     */
    public AdminModel updateAdmin(AdminModel admin) {
        return adminRepository.save(admin);
    }

    /**
     * Delete admin by ID.
     */
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}

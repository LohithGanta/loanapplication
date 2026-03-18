package com.example.demo.repository;

import com.example.demo.entity.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminModel, Long> {
    Optional<AdminModel> findByEmail(String email);
    boolean existsByEmail(String email);
}

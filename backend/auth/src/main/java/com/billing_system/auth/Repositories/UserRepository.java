package com.billing_system.auth.Repositories;

import com.billing_system.auth.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
   Optional<Users> findUsersByEmail(String email);
   Optional<Users> findUsersByPhoneNumber(String phoneNumber);
}

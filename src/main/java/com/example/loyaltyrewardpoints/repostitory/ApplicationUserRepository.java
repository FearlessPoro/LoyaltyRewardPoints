package com.example.loyaltyrewardpoints.repostitory;

import com.example.loyaltyrewardpoints.model.ApplicationUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    @NonNull
    Optional<ApplicationUser> findById(@NonNull final Long Id);

    Optional<ApplicationUser> findByEmail(final String email);
}

package com.mrs.enpoint.feature.savednumber.repository;

import com.mrs.enpoint.entity.SavedMobileNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedNumberRepository extends JpaRepository<SavedMobileNumber, Integer> {

    // All saved numbers for a user
    List<SavedMobileNumber> findByUser_UserId(int userId);

    // Check duplicate: same user + same mobile number
    boolean existsByUser_UserIdAndMobileNumber(int userId, String mobileNumber);

    // Find specific saved number by id and user (ownership check)
    Optional<SavedMobileNumber> findByIdAndUser_UserId(int id, int userId);
}
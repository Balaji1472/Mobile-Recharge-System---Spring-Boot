package com.mrs.enpoint.feature.savednumber.repository;

import com.mrs.enpoint.entity.SavedMobileNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedNumberRepository extends JpaRepository<SavedMobileNumber, Integer> {

    List<SavedMobileNumber> findByUser_UserId(int userId);

    boolean existsByUser_UserIdAndMobileNumber(int userId, String mobileNumber);

    Optional<SavedMobileNumber> findByIdAndUser_UserId(int id, int userId);
}
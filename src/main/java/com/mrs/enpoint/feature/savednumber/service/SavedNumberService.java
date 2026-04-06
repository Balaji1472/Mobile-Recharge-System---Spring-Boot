package com.mrs.enpoint.feature.savednumber.service;

import com.mrs.enpoint.feature.savednumber.dto.SavedNumberRequestDTO;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberResponseDTO;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberUpdateDTO;

import java.util.List;

public interface SavedNumberService {

    List<SavedNumberResponseDTO> getMySavedNumbers();

    SavedNumberResponseDTO saveNumber(SavedNumberRequestDTO request);

    SavedNumberResponseDTO updateNickname(int id, SavedNumberUpdateDTO request);

    void deleteSavedNumber(int id);
}
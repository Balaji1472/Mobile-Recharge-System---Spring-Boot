package com.mrs.enpoint.feature.offer.service;

import java.util.List;

import com.mrs.enpoint.feature.offer.dto.OfferRequestDTO;
import com.mrs.enpoint.feature.offer.dto.OfferResponseDTO;

public interface OfferService {

    OfferResponseDTO createOffer(OfferRequestDTO request);
    
    List<OfferResponseDTO> getAllOffers();
    
    OfferResponseDTO getOfferById(int id);
    
    OfferResponseDTO updateOffer(int id, OfferRequestDTO request);
    
    void deactivate(int id);
    
    void activate(int id);
    
    
    List<OfferResponseDTO> getActiveOffers();

    OfferResponseDTO getActiveOfferById(int id);
}
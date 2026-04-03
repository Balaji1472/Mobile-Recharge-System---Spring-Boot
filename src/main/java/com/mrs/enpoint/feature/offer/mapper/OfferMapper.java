package com.mrs.enpoint.feature.offer.mapper;

import com.mrs.enpoint.entity.Offer;
import com.mrs.enpoint.feature.offer.dto.OfferResponseDTO;

public final class OfferMapper {

    private OfferMapper() {}

    public static OfferResponseDTO toResponseDTO(Offer offer) {

        if (offer == null) {
            return null;
        }

        OfferResponseDTO dto = new OfferResponseDTO();
        dto.setOfferId(offer.getOfferId());
        dto.setTitle(offer.getTitle());
        dto.setDiscountType(offer.getDiscountType());
        dto.setDiscountValue(offer.getDiscountValue());
        dto.setStartDate(offer.getStartDate());
        dto.setEndDate(offer.getEndDate());
        dto.setActive(offer.getIsActive());

        return dto;
    }
}
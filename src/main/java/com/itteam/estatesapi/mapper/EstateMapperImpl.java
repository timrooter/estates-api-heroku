package com.itteam.estatesapi.mapper;

import com.itteam.estatesapi.model.Estate;
import com.itteam.estatesapi.rest.dto.CreateEstateRequest;
import com.itteam.estatesapi.rest.dto.EstateDto;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EstateMapperImpl implements EstateMapper {

    @Override
    public Estate toEstate(CreateEstateRequest createEstateRequest) {
        if (createEstateRequest == null) {
            return null;
        }
        return new Estate(createEstateRequest.getTitle(), createEstateRequest.getPoster(), createEstateRequest.getDescription(), createEstateRequest.getContact(), createEstateRequest.getPrice(), createEstateRequest.getAddress());
    }

    @Override
    public EstateDto toEstateDto(Estate estate) {
        if (estate == null) {
            return null;
        }
        return new EstateDto(estate.getId(), estate.getTitle(), estate.getPoster(), estate.getDescription(), estate.getContact(), estate.getPrice(), estate.getAddress(), DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(estate.getCreatedAt()));
    }
}

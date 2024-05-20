package com.itteam.estatesapi.mapper;

import com.itteam.estatesapi.model.Estate;
import com.itteam.estatesapi.rest.dto.CreateEstateRequest;
import com.itteam.estatesapi.rest.dto.EstateDto;

public interface EstateMapper {

    Estate toEstate(CreateEstateRequest createEstateRequest);

    EstateDto toEstateDto(Estate estate);
}
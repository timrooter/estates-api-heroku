package com.itteam.estatesapi.service;

import com.itteam.estatesapi.model.Estate;

import java.util.List;

public interface EstateService {

    List<Estate> getEstates();

    List<Estate> getEstatesContainingText(String text);

    Estate validateAndGetEstate(String id);

    Estate saveEstate(Estate estate);

    void deleteEstate(Estate estate);
}

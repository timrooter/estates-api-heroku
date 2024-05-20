package com.itteam.estatesapi.rest;

import com.itteam.estatesapi.service.EstateService;
import com.itteam.estatesapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    private final EstateService estateService;

    @GetMapping("/numberOfUsers")
    public Integer getNumberOfUsers() {
        return userService.getUsers().size();
    }

    @GetMapping("/numberOfEstates")
    public Integer getNumberOfEstates() {
        return estateService.getEstates().size();
    }
}



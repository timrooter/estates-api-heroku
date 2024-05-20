package com.itteam.estatesapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @Schema(example = "user")
    @NotBlank
    private String username;

    @Schema(example = "user")
    @NotBlank
    private String password;
}

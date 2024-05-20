package com.itteam.estatesapi.rest;

import com.itteam.estatesapi.mapper.EstateMapper;
import com.itteam.estatesapi.model.Estate;
import com.itteam.estatesapi.rest.dto.CreateEstateRequest;
import com.itteam.estatesapi.rest.dto.EstateDto;
import com.itteam.estatesapi.service.EstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.itteam.estatesapi.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/estates")
public class EstateController {

    private final EstateService estateService;
    private final EstateMapper estateMapper;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public List<EstateDto> getEstates(@RequestParam(value = "text", required = false) String text) {
        List<Estate> estates = (text == null) ? estateService.getEstates() : estateService.getEstatesContainingText(text);
        return estates.stream()
                .map(estateMapper::toEstateDto)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EstateDto createEstate(@Valid @RequestBody CreateEstateRequest createEstateRequest) {
        Estate estate = estateMapper.toEstate(createEstateRequest);
        estate.setId(UUID.randomUUID().toString());
        return estateMapper.toEstateDto(estateService.saveEstate(estate));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{id}")
    public EstateDto deleteEstate(@PathVariable String id) {
        Estate estate = estateService.validateAndGetEstate(id);
        estateService.deleteEstate(estate);
        return estateMapper.toEstateDto(estate);
    }
}

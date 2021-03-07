package com.nazjara.service.service;

import com.nazjara.service.domain.Brewery;
import com.nazjara.service.repository.BreweryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BreweryServiceImpl implements BreweryService{

    private final BreweryRepository breweryRepository;

    @Override
    public List<Brewery> getAllBreweries() {
        return breweryRepository.findAll();
    }
}

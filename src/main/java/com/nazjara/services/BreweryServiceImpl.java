package com.nazjara.services;

import com.nazjara.domain.Brewery;
import com.nazjara.repositories.BreweryRepository;
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

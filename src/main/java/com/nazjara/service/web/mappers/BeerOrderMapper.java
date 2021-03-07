package com.nazjara.service.web.mappers;

import com.nazjara.service.web.model.BeerOrderDto;
import com.nazjara.service.domain.BeerOrder;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class, BeerOrderLineMapper.class})
public interface BeerOrderMapper {

    BeerOrderDto beerOrderToDto(BeerOrder beerOrder);

    BeerOrder dtoToBeerOrder(BeerOrderDto dto);
}

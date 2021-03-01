package com.nazjara.web.mappers;

import com.nazjara.web.model.BeerOrderDto;
import com.nazjara.domain.BeerOrder;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class, BeerOrderLineMapper.class})
public interface BeerOrderMapper {

    BeerOrderDto beerOrderToDto(BeerOrder beerOrder);

    BeerOrder dtoToBeerOrder(BeerOrderDto dto);
}

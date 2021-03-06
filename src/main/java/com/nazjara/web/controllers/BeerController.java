package com.nazjara.web.controllers;

import com.nazjara.security.permissions.BeerCreatePermission;
import com.nazjara.security.permissions.BeerReadPermission;
import com.nazjara.security.permissions.BeerUpdatePermission;
import com.nazjara.service.domain.Beer;
import com.nazjara.service.repository.BeerInventoryRepository;
import com.nazjara.service.repository.BeerRepository;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/beers")
@Controller
public class BeerController {

    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;


    @BeerReadPermission
    @RequestMapping("/find")
    public String findBeers(Model model) {
        model.addAttribute("beer", Beer.builder().build());
        return "beers/findBeers";
    }

    @BeerReadPermission
    @GetMapping
    public String processFindFormReturnMany(Beer beer, BindingResult result, Model model) {
        // find beers by name
        //ToDO: Add Service
        //ToDO: Get paging data from view
        Page<Beer> pagedResult = beerRepository.findAllByBeerNameIsLike("%" + beer.getBeerName() + "%", createPageRequest(0, 10, Sort.Direction.DESC, "beerName"));
        List<Beer> beerList = pagedResult.getContent();
        if (beerList.isEmpty()) {
            // no beers found
            result.rejectValue("beerName", "notFound", "not found");
            return "beers/findBeers";
        } else if (beerList.size() == 1) {
            // 1 beer found
            beer = beerList.get(0);
            return "redirect:/beers/" + beer.getId();
        } else {
            // multiple beers found
            model.addAttribute("selections", beerList);
            return "beers/beerList";
        }
    }


    @BeerReadPermission
    @GetMapping("/{beerId}")
    public ModelAndView showBeer(@PathVariable UUID beerId) {
        ModelAndView mav = new ModelAndView("beers/beerDetails");
        //ToDO: Add Service
        mav.addObject(beerRepository.findById(beerId).get());
        return mav;
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("beer", Beer.builder().build());
        return "beers/createBeer";
    }

    @BeerCreatePermission
    @PostMapping("/new")
    public String processCreationForm(Beer beer) {
        //ToDO: Add Service
        Beer newBeer = Beer.builder()
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .minOnHand(beer.getMinOnHand())
                .price(beer.getPrice())
                .quantityToBrew(beer.getQuantityToBrew())
                .upc(beer.getUpc())
                .build();

        Beer savedBeer = beerRepository.save(newBeer);
        return "redirect:/beers/" + savedBeer.getId();
    }

    @GetMapping("/{beerId}/edit")
    public String initUpdateBeerForm(@PathVariable UUID beerId, Model model) {
        if (beerRepository.findById(beerId).isPresent())
            model.addAttribute("beer", beerRepository.findById(beerId).get());
        return "beers/createOrUpdateBeer";
    }

    @BeerUpdatePermission
    @PostMapping("/{beerId}/edit")
    public String processUpdateForm(@Valid Beer beer, BindingResult result) {
        if (result.hasErrors()) {
            return "beers/createOrUpdateBeer";
        } else {
            //ToDO: Add Service
            Beer savedBeer = beerRepository.save(beer);
            return "redirect:/beers/" + savedBeer.getId();
        }
    }

    private PageRequest createPageRequest(int page, int size, Sort.Direction sortDirection, String propertyName) {
        return PageRequest.of(page,
                size,
                Sort.by(sortDirection, propertyName));
    }
}



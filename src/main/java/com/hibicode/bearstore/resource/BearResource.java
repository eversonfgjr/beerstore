package com.hibicode.bearstore.resource;

import com.hibicode.bearstore.model.Beer;
import com.hibicode.bearstore.repository.Beers;
import com.hibicode.bearstore.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/beers")
public class BearResource {

    @Autowired
    private Beers beers;

    @Autowired
    private BeerService beerService;

    @GetMapping
    public List<Beer> all() {
        return beers.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beer create(@Valid @RequestBody Beer beer) {

        return beerService.save(beer);
    }

    @PutMapping("/{id}")
    public Beer update(@PathVariable Long id,
                       @Valid @RequestBody Beer beer) {
        beer.setId(id);
        return beerService.save(beer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        beerService.delete(id);
    }
}

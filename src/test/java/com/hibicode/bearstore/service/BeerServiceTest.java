package com.hibicode.bearstore.service;

import com.hibicode.bearstore.model.Beer;
import com.hibicode.bearstore.model.BeerType;
import com.hibicode.bearstore.repository.Beers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

public class BeerServiceTest {

    @Mock
    private Beers beersMocked;

    private BeerService beerService;

    @Before
    public void setup(){
        // inicia os mocks que estão nesse objeto
        MockitoAnnotations.initMocks(this);
        beerService = new BeerService(beersMocked);
    }

    @Test(expected = BeerAlreadyExistException.class)
    public void should_deny_creation_of_beer_that_exists() {

        //objeto retornado pelo mock
        Beer beerInDatabase = new Beer();
        beerInDatabase.setId(10l);
        beerInDatabase.setName("Heineken");
        beerInDatabase.setVolume(new BigDecimal("350"));
        beerInDatabase.setType(BeerType.LAGER);

        // import static org.mockito.Mockito.when;
        when(beersMocked.findByNameAndType("Heineken", BeerType.LAGER)).thenReturn(Optional.of(beerInDatabase));

        Beer b = new Beer();
        b.setName("Heineken");
        b.setType(BeerType.LAGER);
        b.setVolume(new BigDecimal("350"));

        beerService.save(b);

    }

    @Test
    public void should_create_new_beer(){

        Beer b = new Beer();
        b.setName("Heineken");
        b.setType(BeerType.LAGER);
        b.setVolume(new BigDecimal("350"));

        Beer newBeerInDatabase = new Beer();
        newBeerInDatabase.setId(10l);
        newBeerInDatabase.setName("Heineken");
        newBeerInDatabase.setType(BeerType.LAGER);
        newBeerInDatabase.setVolume(new BigDecimal("350"));
        when(beersMocked.save(b)).thenReturn(newBeerInDatabase);


        Beer beerSaved = beerService.save(b);
        /*
            import static org.hamcrest.MatcherAssert.assertThat;
            import static org.hamcrest.Matchers.*;
         */
        assertThat(beerSaved.getId(), equalTo(10L));
        assertThat(beerSaved.getName(), equalTo("Heineken"));
        assertThat(beerSaved.getType(), equalTo(BeerType.LAGER));
    }

}

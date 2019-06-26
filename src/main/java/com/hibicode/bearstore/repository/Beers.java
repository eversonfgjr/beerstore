package com.hibicode.bearstore.repository;

import com.hibicode.bearstore.model.Beer;
import com.hibicode.bearstore.model.BeerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Beers extends JpaRepository<Beer, Long> {

    //Spring Data tem os Query Methods que cria uma implementação baseado no nome do método da interface que extende um JpaRepository (ou um CRUDRepository)
    Optional<Beer> findByNameAndType(String name, BeerType type);
}

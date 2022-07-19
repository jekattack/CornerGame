package com.github.jekattack.cornergame.kioskdata;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface KioskRepository extends MongoRepository<Kiosk, String> {

    Optional<Kiosk> findByName(String name);

    Optional<Kiosk> findByGooglePlacesId(String googlePlaceId);
}

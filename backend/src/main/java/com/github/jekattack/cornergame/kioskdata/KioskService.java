package com.github.jekattack.cornergame.kioskdata;

import com.mongodb.MongoWriteException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KioskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KioskService.class);
    private final KioskRepository kioskRepository;

    public List<Kiosk> getAllKiosks() {
        return kioskRepository.findAll();
    }
    public Kiosk createKiosk(Kiosk testKiosk) {
        return kioskRepository.save(testKiosk);
    }

    public void createManyKiosks(List<Kiosk> kioskList) {
        kioskList.stream()
            .forEach((kiosk) -> {
                try {
                    kioskRepository.save(kiosk);
                } catch (DuplicateKeyException e){
                    LOGGER.info("Kiosk already exists: " + kiosk.getName(), e);
            }
        });
    }

}

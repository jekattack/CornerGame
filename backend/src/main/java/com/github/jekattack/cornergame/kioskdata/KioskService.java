package com.github.jekattack.cornergame.kioskdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class KioskService {
    private final KioskRepository kioskRepository;

    public List<Kiosk> getAllKiosks() {
        return kioskRepository.findAll();
    }
    public Kiosk createKiosk(Kiosk testKiosk) {
        return kioskRepository.save(testKiosk);
    }

    public void createManyKiosks(List<Kiosk> kioskList) {
        kioskList
            .forEach((kiosk) -> {
                try {
                    kioskRepository.save(kiosk);
                } catch (DuplicateKeyException e){
                    log.info("Kiosk already exists: " + kiosk.getName(), e);
            }
        });
    }

}

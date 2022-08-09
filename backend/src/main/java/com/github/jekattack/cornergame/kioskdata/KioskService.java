package com.github.jekattack.cornergame.kioskdata;

import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public ActiveQuestDTO getLocationsForQuest(String[] questPlacesIds) {
        List<ActiveQuestWaypointDTO> waypoints = new ArrayList<>();
        KioskLocationCoordinates start = kioskRepository.findByGooglePlacesId(questPlacesIds[0]).stream()
                .findFirst().orElseThrow()
                .getKioskLocation().getLocation();
        KioskLocationCoordinates finish = kioskRepository.findByGooglePlacesId(questPlacesIds[questPlacesIds.length-1]).stream()
                .findFirst().orElseThrow()
                .getKioskLocation().getLocation();
        for(int i=1; i<questPlacesIds.length-1; i++){
                waypoints.add(new ActiveQuestWaypointDTO(kioskRepository.findByGooglePlacesId(questPlacesIds[i]).stream()
                        .findFirst().orElseThrow()
                        .getKioskLocation().getLocation()));
        }
        return new ActiveQuestDTO(start, waypoints, finish);
    }
}

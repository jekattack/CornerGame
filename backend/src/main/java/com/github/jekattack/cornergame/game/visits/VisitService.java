package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitService {

    private final VisitRepository visitRepository;
    private final KioskRepository kioskRepository;
    private final CGUserRepository cgUserRepository;
    private final CGUserGameDataService cgUserGameDataService;
    public void createVisit(VisitCreationData visitCreationData, String username) {

        //User Coordinates
        CGUser user = cgUserRepository.findByUsername(username).orElseThrow();
        double userLocationLat = visitCreationData.getUserLocation().getUserLocationCoordinates().getLat();
        double userLocationLng = visitCreationData.getUserLocation().getUserLocationCoordinates().getLng();

        //Kiosk Coordinates
        Kiosk kioskToVisit = kioskRepository.findByGooglePlacesId(visitCreationData.getGooglePlacesId()).orElseThrow();
        double kioskToVisitLat = kioskToVisit.getKioskLocation().getLocation().getLat();
        double kioskToVisitLng = kioskToVisit.getKioskLocation().getLocation().getLng();

        //Check if User visited Kiosk within the last 24 hours
        List<Visit> allVisitsAtKioskToVisit = Arrays.stream(visitRepository.findAllByUserId(user.getId()))
                .filter(visit -> (visit.getGooglePlacesId()).equals(visitCreationData.getGooglePlacesId())).toList();
        if(!allVisitsAtKioskToVisit.isEmpty()){
            List<Date> timestamps = allVisitsAtKioskToVisit.stream().map(Visit::getTimestamp).toList();
            Date timestampNow = Date.from(Instant.now());
            for(Date timestamp : timestamps){
                timestamp.setTime(timestamp.getTime() + (24*60*60*1000));
                if(timestampNow.before(timestamp)){
                    throw new IllegalStateException();
                }
            }
        }

        //Check if Users Location is valid for visit
        if(kioskToVisitLat - 0.0001 < userLocationLat
                && kioskToVisitLat + 0.0001 > userLocationLat
                && kioskToVisitLng - 0.0001 < userLocationLng
                && kioskToVisitLng + 0.0001 > userLocationLng){
            Visit newVisit = new Visit(null, user.getId(), kioskToVisit.getGooglePlacesId(), Date.from(Instant.now()), null);
            visitRepository.save(newVisit);
            cgUserGameDataService.scoreForNewVisit(user.getId());
        } else {
            throw new IllegalStateException();
        }
    }

    public List<Visit> getUsersVisits(String username) {
        CGUser user = cgUserRepository.findByUsername(username).orElseThrow();
        return Arrays.stream(visitRepository.findAllByUserId(user.getId())).toList();
    }
}

package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.quests.QuestService;
import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitService {

    private final VisitRepository visitRepository;
    private final KioskRepository kioskRepository;
    private final CGUserGameDataService cgUserGameDataService;
    private final QuestService questService;
    private final List<VisitObserver> visitObservers;

    public ArrayList<Visit> getUsersVisits(String userId) {
        return visitRepository.findAllByUserId(userId);
    }
    public String createVisit(VisitCreationData visitCreationData, String userId) {

        //User Coordinates
        double userLat = visitCreationData.getUserLocation().getUserLocationCoordinates().getLat();
        double userLng = visitCreationData.getUserLocation().getUserLocationCoordinates().getLng();

        //Kiosk Coordinates
        Kiosk kiosk = kioskRepository.findByGooglePlacesId(visitCreationData.getGooglePlacesId()).orElseThrow();
        double kioskLat = kiosk.getKioskLocation().getLocation().getLat();
        double kioskLng = kiosk.getKioskLocation().getLocation().getLng();

        checkIfUserDidNotVisitKioskWithin24Hours(userId, visitCreationData);
        validateUsersLocation(kioskLat, kioskLng, userLat, userLng);
        Visit newVisit = new Visit(null, userId, kiosk.getGooglePlacesId(), Date.from(Instant.now()), null);

        cgUserGameDataService.refreshQuestItemsStatus(userId);
        CGUserGameData userGameData = cgUserGameDataService.getByUserId(userId).orElseThrow();

        Optional<Quest> activeQuestWithKioskOptional = questService.returnActiveQuestWithKiosk(userId, kiosk.getGooglePlacesId());
        if(activeQuestWithKioskOptional.isPresent() && activeQuestWithKioskOptional.get().getId()!=null){
            Quest quest = activeQuestWithKioskOptional.get();
            newVisit.setQuestId(quest.getId());
        }

        Visit visitCreated = visitRepository.save(newVisit);
        visitObservers.forEach(visitObserver -> visitObserver.onVisitCreated(visitCreated, userGameData));

        return "Kiosk besucht!";
    }

    private void checkIfUserDidNotVisitKioskWithin24Hours(String userId, VisitCreationData visitCreationData){
        if(visitRepository.existsByUserIdAndAndGooglePlacesIdAndTimestampAfter(
                userId,
                visitCreationData.getGooglePlacesId(),
                Date.from(Instant.now().minus(Duration.ofHours(24))))){
            throw new IllegalStateException("Kiosk already visited within last 24h");
        }
    }
    private void validateUsersLocation(double kioskLocationLat, double kioskLocationLng, double userLocationLat, double userLocationLng){
        if(!(kioskLocationLat - 0.0001 < userLocationLat
                && kioskLocationLat + 0.0001 > userLocationLat
                && kioskLocationLng - 0.0001 < userLocationLng
                && kioskLocationLng + 0.0001 > userLocationLng)){
            throw new IllegalStateException("Users location is not adequate");
        }
    }
}

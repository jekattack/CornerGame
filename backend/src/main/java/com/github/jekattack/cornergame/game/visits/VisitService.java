package com.github.jekattack.cornergame.game.visits;

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
    private final List<VisitObserver> visitObservers;

    public ArrayList<Visit> getUsersVisits(String userId) {
        return visitRepository.findAllByUserId(userId);
    }
    public String createVisit(VisitCreationData visitCreationData, String userId) {
        cgUserGameDataService.refreshQuestItemsStatus(userId);

        validateUsersLocation(visitCreationData);
        checkIfUserDidNotVisitKioskWithin24Hours(userId, visitCreationData);

        Visit newVisit = new Visit(null, userId, visitCreationData.getGooglePlacesId(), Date.from(Instant.now()), null);
        setQuestIdIfVisitIsPartOfActiveQuest(userId, visitCreationData, newVisit);

        Visit visitCreated = visitRepository.save(newVisit);
        visitObservers.forEach(visitObserver -> visitObserver.onVisitCreated(visitCreated));

        return "Kiosk besucht!";
    }

    private void setQuestIdIfVisitIsPartOfActiveQuest(String userId, VisitCreationData visitCreationData, Visit newVisit) {
        Optional<Quest> activeQuestForKiosk = cgUserGameDataService.getActiveQuestForKiosk(userId, visitCreationData.getGooglePlacesId());
        activeQuestForKiosk.ifPresent(quest -> newVisit.setQuestId(quest.getId()));
    }

    private void checkIfUserDidNotVisitKioskWithin24Hours(String userId, VisitCreationData visitCreationData){
        if(visitRepository.existsByUserIdAndAndGooglePlacesIdAndTimestampAfter(
                userId,
                visitCreationData.getGooglePlacesId(),
                Date.from(Instant.now().minus(Duration.ofHours(24))))){
            throw new IllegalStateException("Kiosk already visited within last 24h");
        }
    }
    private void validateUsersLocation(VisitCreationData visitCreationData){

        double userLat = visitCreationData.getUserLocation().getUserLocationCoordinates().getLat();
        double userLng = visitCreationData.getUserLocation().getUserLocationCoordinates().getLng();

        Kiosk kiosk = kioskRepository.findByGooglePlacesId(visitCreationData.getGooglePlacesId()).orElseThrow();
        double kioskLat = kiosk.getKioskLocation().getLocation().getLat();
        double kioskLng = kiosk.getKioskLocation().getLocation().getLng();

        if(!(kioskLat - 0.0001 < userLat
                && kioskLat + 0.0001 > userLat
                && kioskLng - 0.0001 < userLng
                && kioskLng + 0.0001 > userLng)){
            throw new IllegalStateException("Users location is not adequate");
        }
    }
}

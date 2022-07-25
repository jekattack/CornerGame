package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
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

    public String createVisit(VisitCreationData visitCreationData, String userId) {

        //User Coordinates
        double userLocationLat = visitCreationData.getUserLocation().getUserLocationCoordinates().getLat();
        double userLocationLng = visitCreationData.getUserLocation().getUserLocationCoordinates().getLng();

        //Kiosk Coordinates
        Kiosk kioskToVisit = kioskRepository.findByGooglePlacesId(visitCreationData.getGooglePlacesId()).orElseThrow();
        double kioskToVisitLat = kioskToVisit.getKioskLocation().getLocation().getLat();
        double kioskToVisitLng = kioskToVisit.getKioskLocation().getLocation().getLng();

        checkIfUserDidNotVisitKioskWithin24Hours(userId, visitCreationData);
        validateUsersLocation(kioskToVisitLat, kioskToVisitLng, userLocationLat, userLocationLng);
        Visit newVisit = new Visit(null, userId, kioskToVisit.getGooglePlacesId(), Date.from(Instant.now()), null);

        Optional<Quest> activeQuestWithKioskOptional = questService.returnActiveQuestWithKiosk(userId, kioskToVisit.getGooglePlacesId());
        CGUserGameData userGameData = cgUserGameDataService.getByUserId(userId).orElseThrow();

        if(activeQuestWithKioskOptional.isPresent() && activeQuestWithKioskOptional.get().getId()!=null){
            Quest quest = activeQuestWithKioskOptional.get();
            newVisit.setQuestId(quest.getId());
            visitRepository.save(newVisit);
            cgUserGameDataService.scoreForNewVisit(userId);
            List<Visit> questVisits = visitRepository.findAllByQuestIdAndTimestampIsAfter(
                    newVisit.getQuestId(),
                    userGameData.getQuestItems().stream()
                    .filter(qi -> qi.getQuestId().equals(quest.getId()))
                    .findFirst().get().getTimestamp());
            if(questService.checkIfQuestComplete(questVisits, quest.getId())){
                cgUserGameDataService.scoreForQuestAndMarkAsDone(userId, quest);
                return "Kiosk besucht und Quest " + quest.getName() + " abgeschlossen!";
            }
            return "Kiosk besucht und Quest " + quest.getName() + " zugeordnet!";
        }

        visitRepository.save(newVisit);
        cgUserGameDataService.scoreForNewVisit(userId);

        return "Kiosk besucht!";
    }

    public ArrayList<Visit> getUsersVisits(String userId) {
        return visitRepository.findAllByUserId(userId);
    }
}

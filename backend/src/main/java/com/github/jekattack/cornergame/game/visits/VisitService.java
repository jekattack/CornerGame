package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
import com.github.jekattack.cornergame.game.quests.QuestService;
import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitService {

    private final VisitRepository visitRepository;
    private final KioskRepository kioskRepository;
    private final CGUserGameDataService cgUserGameDataService;
    private final QuestService questService;
    public void createVisit(VisitCreationData visitCreationData, String userId) {

        //User Coordinates
        double userLocationLat = visitCreationData.getUserLocation().getUserLocationCoordinates().getLat();
        double userLocationLng = visitCreationData.getUserLocation().getUserLocationCoordinates().getLng();

        //Kiosk Coordinates
        Kiosk kioskToVisit = kioskRepository.findByGooglePlacesId(visitCreationData.getGooglePlacesId()).orElseThrow();
        double kioskToVisitLat = kioskToVisit.getKioskLocation().getLocation().getLat();
        double kioskToVisitLng = kioskToVisit.getKioskLocation().getLocation().getLng();

        //Check if User did not visit Kiosk within the last 24 hours
        List<Visit> allVisitsAtKioskToVisit = visitRepository.findAllByUserId(userId).stream()
                .filter(visit -> (visit.getGooglePlacesId()).equals(visitCreationData.getGooglePlacesId())).toList();
        if(!allVisitsAtKioskToVisit.isEmpty()){
            List<Date> timestamps = allVisitsAtKioskToVisit.stream().map(Visit::getTimestamp).toList();
            Date timestampNow = Date.from(Instant.now());
            for(Date timestamp : timestamps){
                timestamp.setTime(timestamp.getTime() + (24*60*60*1000));
                if(timestampNow.before(timestamp)){
                    throw new IllegalStateException("Kiosk already visited within last 24h");
                }
            }
        }

        //Check if Users Location is valid for visit
        if(!(kioskToVisitLat - 0.0001 < userLocationLat
                && kioskToVisitLat + 0.0001 > userLocationLat
                && kioskToVisitLng - 0.0001 < userLocationLng
                && kioskToVisitLng + 0.0001 > userLocationLng)){
            throw new IllegalStateException("Users location is not adequate");
        }

        Visit newVisit = new Visit(null, userId, kioskToVisit.getGooglePlacesId(), Date.from(Instant.now()), null);

        ArrayList<ActiveQuestDTO> activeQuestsDTO = questService.getActiveQuests(userId);
        for(ActiveQuestDTO questDTO : activeQuestsDTO){
            String[] kioskGooglePlacesIds = questDTO.getQuest().getKioskGooglePlacesIds();
            if(Arrays.asList(kioskGooglePlacesIds).contains(newVisit.getGooglePlacesId())){
                //Visit mit QuestId hinzufügen
                newVisit.setQuestId(questDTO.getQuest().getId());
                visitRepository.save(newVisit);
                cgUserGameDataService.scoreForNewVisit(userId);
            //Checken, ob Quest abgeschlossen ist
                //Alle questbezogenen Visits laden:
                List<Visit> visitsForQuestId = visitRepository.findAllByQuestId(questDTO.getQuest().getId());
                // Visits filtern:
                // Bedingung, dass Visits für Quest-Durchlauf gültig sind:
                // Zeitpunkt des Visits muss nach dem
                // Startzeitpunkt des Quests liegen
                CGUserGameData userGameData = cgUserGameDataService.getByUserId(userId).orElseThrow();
                List<QuestItem> usersQuestItems = userGameData.getQuestItems();
                QuestItem usersQuestItemForQuest = usersQuestItems.stream()
                        .filter(qi -> qi.getQuestId().equals(questDTO.getQuest().getId()))
                        .findFirst().get();
                Date questStartedAt = usersQuestItemForQuest.getTimestamp();
                List<Visit> relevantVisitsForQuestId = visitsForQuestId.stream().filter(visit -> visit.getTimestamp().after(questStartedAt)).toList();
                // Eventuelle mehrfachbesuche aussortieren
                List<String> forQuestVisitedKiosks = relevantVisitsForQuestId.stream().map(visit -> visit.getGooglePlacesId()).distinct().toList();

                //Check, ob alle Kioske besucht wurden:
                if (forQuestVisitedKiosks.size()==questDTO.getQuest().getKioskGooglePlacesIds().length){

                    //Wenn ja, setze Quest-Status auf DONE
                    int indexOfQuestItem = usersQuestItems.indexOf(usersQuestItemForQuest);
                    usersQuestItemForQuest.setQuestStatus(QuestStatus.DONE);
                    usersQuestItems.set(indexOfQuestItem, usersQuestItemForQuest);
                    userGameData.setQuestItems(new ArrayList<>(usersQuestItems));
                    cgUserGameDataService.save(userGameData);

                    //Add bonus points for finishing quest
                    cgUserGameDataService.scoreForQuest(userId, questDTO.getQuest().getScoreMultiplier(), forQuestVisitedKiosks.size());
                    break;
                }


            }
        }
        //Visit hinzufügen, wenn er zu keinem aktiven Quest gehört
        visitRepository.save(newVisit);
        cgUserGameDataService.scoreForNewVisit(userId);

    }

    public ArrayList<Visit> getUsersVisits(String userId) {
        return visitRepository.findAllByUserId(userId);
    }
}

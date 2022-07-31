package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.model.CGErrorDTO;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllQuests(){
        try{
            return ResponseEntity.ok().body(questService.getAllQuests());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addQuest(@RequestBody Quest newQuest){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(questService.addQuest(newQuest));
        } catch (DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Quests not created", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Object> startQuest(@RequestBody String questId, Principal principal){
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(questService.startQuest(principal.getName(), questId));
        } catch (NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Quest not started", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Object> cancelQuest(@RequestBody String questId, Principal principal){
        try {
            //principal.getName() contains userId
            return ResponseEntity.status(HttpStatus.GONE).body(questService.cancelQuest(principal.getName(), questId));
        } catch (NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Quest not canceled", e.getMessage(), "Quest not found or already expired"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }
}

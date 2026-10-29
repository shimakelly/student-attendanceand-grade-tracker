package auca.tracker.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.tracker.domain.ScoreEntry;
import auca.tracker.service.ScoreEntryService;

@RestController
@RequestMapping("/api/score-entries")
public class ScoreEntryController {

    @Autowired
    private ScoreEntryService scoreEntryService;

    // E1: Write a score entry
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveScoreEntry(@RequestBody ScoreEntry scoreEntry, @RequestParam UUID assessmentId) {
        String message = scoreEntryService.saveScoreEntry(scoreEntry, assessmentId);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("only for GRADED")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // E2: Score entry history of a student
    @GetMapping(value = "/student/{studentNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getScoreEntriesByStudent(@PathVariable String studentNumber) {
        List<ScoreEntry> scoreEntries = scoreEntryService.getScoreEntriesByStudentNumber(studentNumber);
        return new ResponseEntity<>(scoreEntries, HttpStatus.OK);
    }

    // E3: Delete score entry
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteScoreEntry(@PathVariable UUID id) {
        String message = scoreEntryService.deleteScoreEntry(id);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllScoreEntries() {
        List<ScoreEntry> scoreEntries = scoreEntryService.getAllScoreEntries();
        return new ResponseEntity<>(scoreEntries, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getScoreEntryById(@PathVariable UUID id) {
        Optional<ScoreEntry> scoreEntry = scoreEntryService.getScoreEntryById(id);
        if (scoreEntry.isEmpty()) {
            return new ResponseEntity<>("The score entry with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scoreEntry.get(), HttpStatus.OK);
    }
}

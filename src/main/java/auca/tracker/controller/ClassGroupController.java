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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.tracker.domain.ClassGroup;
import auca.tracker.service.ClassGroupService;

@RestController
@RequestMapping("/api/class-groups")
public class ClassGroupController {

    @Autowired
    private ClassGroupService classGroupService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveClassGroup(@RequestBody ClassGroup classGroup) {
        String message = classGroupService.saveClassGroup(classGroup);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllClassGroups() {
        List<ClassGroup> groups = classGroupService.getAllClassGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClassGroupById(@PathVariable UUID id) {
        Optional<ClassGroup> classGroup = classGroupService.getClassGroupById(id);
        if (classGroup.isEmpty()) {
            return new ResponseEntity<>("The class group with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(classGroup.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateClassGroup(@PathVariable UUID id, @RequestBody ClassGroup classGroup) {
        String message = classGroupService.updateClassGroup(id, classGroup);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteClassGroup(@PathVariable UUID id) {
        String message = classGroupService.deleteClassGroup(id);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // C2: Free class groups
    @GetMapping(value = "/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFreeClassGroups() {
        List<ClassGroup> freeGroups = classGroupService.getFreeClassGroups();
        return new ResponseEntity<>(freeGroups, HttpStatus.OK);
    }
}

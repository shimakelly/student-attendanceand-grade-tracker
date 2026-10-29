package auca.tracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auca.tracker.domain.ClassGroup;
import auca.tracker.repository.ClassGroupRepository;

@Service
public class ClassGroupService {
    @Autowired
    private ClassGroupRepository classGroupRepo;

    public String saveClassGroup(ClassGroup classGroup) {
        classGroupRepo.save(classGroup);
        return "Class group saved successfully";
    }

    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepo.findAll();
    }

    public Optional<ClassGroup> getClassGroupById(UUID id) {
        return classGroupRepo.findById(id);
    }

    public String updateClassGroup(UUID id, ClassGroup updatedClassGroup) {
        Optional<ClassGroup> optionalClassGroup = classGroupRepo.findById(id);
        if (optionalClassGroup.isEmpty()) {
            return "The class group with that id does not exist";
        }
        ClassGroup existing = optionalClassGroup.get();
        existing.setGroupNumber(updatedClassGroup.getGroupNumber());
        existing.setName(updatedClassGroup.getName());
        classGroupRepo.save(existing);
        return "Class group updated successfully";
    }

    public String deleteClassGroup(UUID id) {
        Optional<ClassGroup> optionalClassGroup = classGroupRepo.findById(id);
        if (optionalClassGroup.isEmpty()) {
            return "The class group with that id does not exist";
        }
        classGroupRepo.deleteById(id);
        return "Class group deleted successfully";
    }
}

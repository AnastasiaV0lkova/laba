package mongodb.controllers;

import mongodb.models.Classes;
import mongodb.payload.request.ClassesRequest;
import mongodb.repository.ClassesRepository;
import mongodb.services.IClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mongodb.exception.ClassNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/class")
public class ClassesController {

    private final IClassesService classService;
    private final ClassesRepository subjRepository;

    @Autowired
    public ClassesController(IClassesService classService, ClassesRepository subjRepository) {
        this.classService = classService;
        this.subjRepository = subjRepository;
    }

    @PostMapping("/createClass")
    public ResponseEntity<?> createClass(@Valid @RequestBody ClassesRequest classesRequest) {
        if (classService.existsByNumberAndName(classesRequest.getNumber(), classesRequest.getName())) {
            throw new RuntimeException("This class is already exist");
        } else {
            Classes classes = classService.createClass(classesRequest);
            return ResponseEntity.ok().body(classes);
        }
    }

    @GetMapping("/classes")
    public List<Classes> allSubj() {
        return subjRepository.findAll();
    }

    @DeleteMapping("deleteClass/{id}")
    public String deleteSubj(@PathVariable String id) {
        classService.deleteById(id);
        return id;
    }

    @PutMapping("/updateClass/{id}")
    public Classes updateClass(@PathVariable String id, @RequestBody Classes classes) {
        Optional<Classes> byId = subjRepository.findById(id);
        if (byId.isPresent()) {
            Classes updateClasses = byId.get();
            updateClasses.setName(classes.getName());
            updateClasses.setNumber(classes.getNumber());
            return subjRepository.save(updateClasses);
        } else {
            throw new ClassNotFoundException("Class not found");
        }
    }
}

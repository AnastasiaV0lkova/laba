package mongodb.services;

import mongodb.models.Classes;
import mongodb.payload.request.ClassesRequest;
import mongodb.repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesServiceImpl implements IClassesService {

    private final ClassesRepository classesRepository;

    @Autowired
    public ClassesServiceImpl(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    @Override
    public Classes createClass(ClassesRequest subjRequest) {
        Classes subject = new Classes(subjRequest.getName(), subjRequest.getNumber());
        return classesRepository.save(subject);
    }

    @Override
    public Boolean existsByNumberAndName(String number, String name) {
        return classesRepository.existsByNumberAndName(number, name);
    }

    @Override
    public String deleteById(String id) {
        classesRepository.deleteById(id);
        return id;
    }

    @Override
    public Optional<Classes> findById(String id) {
        Optional<Classes> byId = classesRepository.findById(id);
        return byId;
    }

    @Override
    public List<Classes> findByNumber(String number) {
        List<Classes> byNumber = classesRepository.findByNumber(number);
        return byNumber;
    }

    @Override
    public List<Classes> findByName(String name) {
        return classesRepository.findByName(name);
    }
}

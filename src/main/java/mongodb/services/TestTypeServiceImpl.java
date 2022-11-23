package mongodb.services;

import mongodb.models.ETestStatus;
import mongodb.models.TestStatus;
import mongodb.repository.TestTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestTypeServiceImpl implements ITestTypeService {

    private final TestTypeRepository testTypeRepository;

    @Autowired
    public TestTypeServiceImpl(TestTypeRepository TestTypeRepository) {
        this.testTypeRepository = TestTypeRepository;
    }

    @Override
    public Boolean existsByName(String name) {
        return testTypeRepository.existsByName(name);
    }

    @Override
    public String deleteById(String id) {
        testTypeRepository.deleteById(id);
        return id;
    }

    @Override
    public Optional<TestStatus> findById(String id) {
        return testTypeRepository.findById(id);
    }

    @Override
    public Optional<TestStatus> findByName(ETestStatus name) {
        return testTypeRepository.findByName(name);
    }

    @Override
    public List<TestStatus> findAll() {
        return testTypeRepository.findAll();
    }

}

package mongodb.services;

import mongodb.models.ETestStatus;
import mongodb.models.TestStatus;

import java.util.List;
import java.util.Optional;

public interface ITestTypeService {

    Boolean existsByName(String name);

    String deleteById(String id);

    Optional<TestStatus> findById(String id);

    Optional<TestStatus> findByName(ETestStatus name);

    List<TestStatus> findAll();

}

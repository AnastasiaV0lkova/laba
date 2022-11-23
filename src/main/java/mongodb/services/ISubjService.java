package mongodb.services;

import mongodb.models.Subject;
import mongodb.payload.request.SubjRequest;

import java.util.Optional;

public interface ISubjService {

    Subject createSubj(SubjRequest subjRequest);

    Boolean existsByName(String username);

    String deleteById(String id);

    Optional<Subject> findById(String id);
}

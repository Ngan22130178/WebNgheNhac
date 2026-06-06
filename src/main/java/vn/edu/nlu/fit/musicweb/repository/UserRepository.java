package vn.edu.nlu.fit.musicweb.repository;

import vn.edu.nlu.fit.musicweb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}

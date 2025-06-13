package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}

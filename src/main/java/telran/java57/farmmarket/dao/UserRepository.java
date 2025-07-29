package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.model.User;


public interface UserRepository extends MongoRepository<User,String> {
}

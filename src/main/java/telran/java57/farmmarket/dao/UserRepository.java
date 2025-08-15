package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.model.UserAccount;


public interface UserRepository extends MongoRepository<UserAccount,String> {
}

package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.model.RefreshTokenEntity;

public interface RefreshTokenRepository extends MongoRepository<RefreshTokenEntity,String> {

}

package co.com.pragma.apiphotos.model.persistence.repo;

import co.com.pragma.apiphotos.model.persistence.entity.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepo extends MongoRepository<Photo, String>, QuerydslPredicateExecutor<Photo> {
}
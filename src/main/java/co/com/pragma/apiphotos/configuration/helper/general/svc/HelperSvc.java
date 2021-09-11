package co.com.pragma.apiphotos.configuration.helper.general.svc;

/**
 * @param <ValueObject> value object for entity response class
 * @param <Entity>      entity class
 * @param <TypeId>      type of id(Integer, Long, Etc)
 * @param <Error>       throw error
 */
public interface HelperSvc<ValueObject, Entity, TypeId, Error extends Exception> {
   ValueObject get(Entity model);

   Entity get(TypeId id, String errorMessage) throws Error;
}
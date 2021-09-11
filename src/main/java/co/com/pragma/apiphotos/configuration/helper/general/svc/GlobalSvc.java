package co.com.pragma.apiphotos.configuration.helper.general.svc;

import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @param <ValueObject>              valor del objeto a retornar
 * @param <DataTransferObject>       objeto de transferencia de datos (simples)
 * @param <DataTransferObjectSearch> objeto de transferencia de datos (búsqueda)
 * @param <TypeId>                   Tipo de dato de la entidad
 */
public interface GlobalSvc<ValueObject, DataTransferObject, DataTransferObjectSearch, TypeId> {
   Response<ValueObject> get(TypeId id) throws GlobalException;

   Response<ValueObject> add(DataTransferObject model) throws GlobalException;

   Response<ValueObject> edit(DataTransferObject model, TypeId id) throws GlobalException;

   Response<ValueObject> del(TypeId id) throws GlobalException;

   Page<ValueObject> page(DataTransferObjectSearch model, Pageable pageable);

   List<ValueObject> all(DataTransferObjectSearch model, Integer limit);
}
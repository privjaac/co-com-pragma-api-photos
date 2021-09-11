package co.com.pragma.apiphotos.configuration.helper.general.svc;


import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ControllerSvc<ValueObject, DataTransferObject, DataTransferObjectSearch, TypeId> {
   @GetMapping(value = "/{id}")
   Response<ValueObject> get(@PathVariable TypeId id) throws GlobalException;

   @PostMapping(value = "/")
   Response<ValueObject> add(DataTransferObject model) throws GlobalException;

   @PutMapping(value = "/{id}")
   Response<ValueObject> edit(DataTransferObject model, @PathVariable TypeId id) throws GlobalException;

   @DeleteMapping(value = "/{id}")
   Response<ValueObject> del(@PathVariable TypeId id) throws GlobalException;

   @GetMapping(value = "/page/all")
   Page<ValueObject> page(
           @ModelAttribute DataTransferObjectSearch model,
           @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
           @RequestParam(required = false, defaultValue = "100") Integer pageSize
   );

   @GetMapping(value = "/")
   List<ValueObject> all(@ModelAttribute DataTransferObjectSearch model, Integer limit);
}
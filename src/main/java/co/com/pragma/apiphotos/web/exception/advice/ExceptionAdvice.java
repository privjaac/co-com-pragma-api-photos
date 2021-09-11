package co.com.pragma.apiphotos.web.exception.advice;

import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {
// ResponseEntityExceptionHandler

   @ExceptionHandler(GlobalException.class)
   public Response<?> handleNotFoundException(GlobalException ge) {
      ge.getResponse().setMessage(ge.getMessage());
      ge.getResponse().setFlag(ge.getResponse().getFlag() != null ? ge.getResponse().getFlag() : false);
      ge.getResponse().setCode(ge.getResponse().getCode() != null ? ge.getResponse().getCode() : "500");
      return ge.getResponse();
   }

   @ResponseBody
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<Response<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
      var nombreObjeto = ex.getBindingResult().getObjectName();
      var model = new ObjectMapper().convertValue(ex.getBindingResult().getModel().get(nombreObjeto), Map.class);
      model.forEach((atributo, valor) -> {
         var errores = new ArrayList<String>();
         ex.getBindingResult().getFieldErrors(atributo.toString()).forEach(error -> errores.add(error.getDefaultMessage()));
         model.put(atributo, errores);
      });
      var atributosVacios = (List<String>) model.keySet().stream().filter(atributo -> ((List) model.get(atributo)).isEmpty()).collect(Collectors.toList());
      atributosVacios.forEach(atributosVacio -> model.remove(atributosVacio));
      return ResponseEntity.badRequest().body(Response.builder().flag(false).code("400").message("El objeto json enviado tiene observaciones.").entity(model).build());
   }
}
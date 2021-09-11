package co.com.pragma.apiphotos.web.exception;

import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalException extends Exception {
   private static final long serialVersionUID = 1L;
   private Response<?> response;

   public GlobalException(String message, Response<?> response) {
      super(message);
      this.response = response;
      this.response.setMessage(message);
      this.response.setFlag(response.getFlag() != null ? response.getFlag() : false);
      this.response.setCode(response.getCode() != null ? response.getCode() : "500");
   }

   public GlobalException(String message) {
      super(message);
      this.response = Response.builder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())).flag(false).message(message).build();
   }
}
package co.com.pragma.apiphotos.configuration.helper.general.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class Response<T> {
   private String code;
   private String message;
   private Boolean flag;
   private T entity;
}
package co.com.pragma.apiphotos.model.dto.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoSearchDto implements Serializable {
   private static final long serialVersionUID = -105419390553670856L;
   private String clientId;
   private String photo;
   private Boolean status;
   private String created;
   private String updated;
}
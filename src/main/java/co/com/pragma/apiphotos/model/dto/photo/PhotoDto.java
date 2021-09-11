package co.com.pragma.apiphotos.model.dto.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoDto implements Serializable {
   private static final long serialVersionUID = -5106608591909291545L;
   private UUID clientId;
   private FilePart photo;
   private Boolean status;
   @Default
   private String photoBase64 = "none";
}
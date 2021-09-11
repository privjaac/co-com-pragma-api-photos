package co.com.pragma.apiphotos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoVo implements Serializable {
   private static final long serialVersionUID = 7400199065367208137L;
   private String id;
   private UUID clientId;
   private String photo;
   private Boolean status;
   private String created;
   private String updated;
}
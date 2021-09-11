package co.com.pragma.apiphotos.model.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Document(collection = "photos")
public class Photo implements Serializable {
   private static final long serialVersionUID = 9043810642757839764L;
   @Id
   private String id;
   private UUID clientId;
   private Binary photo;
   private Boolean status;
   @CreatedDate
   private LocalDateTime created;
   @LastModifiedDate
   private LocalDateTime updated;
}
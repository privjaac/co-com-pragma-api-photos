package co.com.pragma.apiphotos.configuration.helper;

import co.com.pragma.apiphotos.configuration.helper.general.svc.HelperSvc;
import co.com.pragma.apiphotos.model.persistence.entity.Photo;
import co.com.pragma.apiphotos.model.persistence.repo.PhotoRepo;
import co.com.pragma.apiphotos.model.vo.PhotoVo;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Service
public class PhotoHelper implements HelperSvc<PhotoVo, Photo, String, GlobalException> {
   private final GlobalHelper helper;
   private final PhotoRepo repo;

   @Lazy
   public PhotoHelper(GlobalHelper helper, PhotoRepo repo) {
      this.helper = helper;
      this.repo = repo;
   }

   @Override
   public PhotoVo get(Photo model) {
      if (!helper.isNotNull(model)) return null;
      return PhotoVo.builder()
              .id(model.getId())
              .clientId(model.getClientId())
              .photo(helper.isNotNull(model.getPhoto()) ? Base64.getEncoder().encodeToString(model.getPhoto().getData()) : null)
              .status(model.getStatus())
              .created(helper.emitNullDateTimeIN(model.getCreated()))
              .updated(helper.emitNullDateTimeIN(model.getUpdated()))
              .build();
   }

   @Override
   public Photo get(String uuid, String errorMessage) throws GlobalException {
      return helper.getEntity(uuid, errorMessage, repo);
   }
}
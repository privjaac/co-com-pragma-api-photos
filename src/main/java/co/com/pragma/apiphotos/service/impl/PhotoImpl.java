package co.com.pragma.apiphotos.service.impl;

import co.com.pragma.apiphotos.configuration.constant.MessageConstant;
import co.com.pragma.apiphotos.configuration.constant.PathConstant;
import co.com.pragma.apiphotos.configuration.helper.GlobalHelper;
import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.model.dto.photo.PhotoDto;
import co.com.pragma.apiphotos.model.dto.photo.PhotoSearchDto;
import co.com.pragma.apiphotos.model.persistence.entity.Photo;
import co.com.pragma.apiphotos.model.persistence.entity.QPhoto;
import co.com.pragma.apiphotos.model.persistence.repo.PhotoRepo;
import co.com.pragma.apiphotos.model.vo.PhotoVo;
import co.com.pragma.apiphotos.service.svc.PhotoSvc;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhotoImpl implements PhotoSvc {
   private final GlobalHelper helper;
   private final PhotoRepo repo;
   public static final String entityName = "Photo's";

   @Lazy
   public PhotoImpl(GlobalHelper helper, PhotoRepo repo) {
      this.helper = helper;
      this.repo = repo;
   }

   @Override
   public Response<PhotoVo> get(String uuid) throws GlobalException {
      var response = helper.basicResponse(PhotoVo.class, MessageConstant.existSuccess.apply(entityName));
      var photo = helper.getPhoto().get(uuid, MessageConstant.existError.apply(entityName));
      response.setEntity(helper.getPhoto().get(photo));
      return response;
   }

   @Override
   public Response<PhotoVo> add(PhotoDto model) throws GlobalException {
      var response = helper.basicResponse(PhotoVo.class, MessageConstant.addSuccess.apply(entityName));
      if (!helper.isNotNull(model.getClientId())) throw new GlobalException(MessageConstant.fieldRequired.apply("clientId"));
      if (!helper.isNotNull(model.getStatus())) throw new GlobalException(MessageConstant.fieldRequired.apply("status"));
      var photo = this.fillField(model, new Photo());
      repo.save(photo);
      response.setEntity(helper.getPhoto().get(photo));
      return response;
   }

   @Override
   public Response<PhotoVo> edit(PhotoDto model, String uuid) throws GlobalException {
      var response = helper.basicResponse(PhotoVo.class, MessageConstant.editSuccess.apply(entityName));
      var photo = this.fillField(model, helper.getPhoto().get(uuid, MessageConstant.editError.apply(entityName)));
      repo.save(photo);
      response.setEntity(helper.getPhoto().get(photo));
      return response;
   }

   @Override
   public Response<PhotoVo> del(String uuid) throws GlobalException {
      var response = helper.basicResponse(PhotoVo.class, MessageConstant.deleteSuccess.apply(entityName));
      var photo = helper.getPhoto().get(uuid, MessageConstant.existError.apply(entityName));
      repo.delete(photo);
      response.setEntity(helper.getPhoto().get(photo));
      return response;
   }

   @Override
   public Page<PhotoVo> page(PhotoSearchDto model, Pageable pageable) {
      var condition = this.listCondition(model);
      var page = repo.findAll(condition, pageable);
      page = helper.getPageImpl(page, pageable);
      return page.map(helper.getPhoto()::get);
   }

   @Override
   public List<PhotoVo> all(PhotoSearchDto model, Integer limit) {
      var tb = QPhoto.photo1;
      var condition = this.listCondition(model);
      var listBase = new ArrayList<Photo>();
      repo.findAll(condition).iterator().forEachRemaining(listBase::add);
      var listStream = listBase.stream();
      if (helper.isNotNull(limit)) listStream = listStream.limit(limit);
      return listStream.map(helper.getPhoto()::get).collect(Collectors.toList());
   }

   private Photo fillField(PhotoDto model, Photo photo) {
      if (helper.isNotNull(model.getClientId())) photo.setClientId(model.getClientId());
      if (helper.isNotNull(model.getStatus())) photo.setStatus(model.getStatus());
      Function<PhotoDto, byte[]> getBytesImageFromFilePart = (dto) -> {
         var photoFile = new File(PathConstant.PATH_TEMP.concat(model.getPhoto().filename()));
         dto.getPhoto().transferTo(photoFile).toProcessor().block();
         return helper.getBytesFromFile(photoFile);
      };
      Function<PhotoDto, byte[]> getBytesImageFromBase64 = (dto) -> Base64.getDecoder().decode(model.getPhotoBase64());
      Function<PhotoDto, Binary> getBinaryImage = (dto) -> {
         byte[] bytesPhoto = helper.isNotNull(model.getPhoto()) ? getBytesImageFromFilePart.apply(model) : getBytesImageFromBase64.apply(model);
         return new Binary(BsonBinarySubType.BINARY, bytesPhoto);
      };
      photo.setPhoto(getBinaryImage.apply(model));
      return photo;
   }

   private Predicate listCondition(PhotoSearchDto model) {
      var tb = QPhoto.photo1;
      var condition = new BooleanBuilder();
      if (helper.isNotNull(model.getClientId())) condition.and(Expressions.stringOperation(Ops.STRING_CAST, tb.clientId).contains(model.getClientId()));
      if (helper.isNotNull(model.getPhoto())) condition.and(Expressions.stringOperation(Ops.STRING_CAST, tb.photo).contains(model.getPhoto()));
      if (helper.isNotNull(model.getStatus())) condition.and(tb.status.eq(model.getStatus()));
      if (helper.isNotNull(model.getCreated())) condition.and(tb.created.stringValue().contains(model.getCreated()));
      if (helper.isNotNull(model.getUpdated())) condition.and(tb.updated.stringValue().contains(model.getUpdated()));
      return condition;
   }

   @Override
   public Response<PhotoVo> addEdit(PhotoDto model) throws GlobalException {
      var photo = this.findPhotoByClientId(model.getClientId().toString(), null).orElse(null);
      if (!helper.isNotNull(photo)) return this.add(model);
      assert photo != null;
      return this.edit(model, photo.getId());
   }

   @Override
   public Response<PhotoVo> oneByClientId(String id) throws GlobalException {
      var response = helper.basicResponse(PhotoVo.class, MessageConstant.existSuccess.apply(entityName));
      var photo = this.findPhotoByClientId(id, true).orElse(null);
      if (!helper.isNotNull(photo)) throw new GlobalException(MessageConstant.existError.apply(entityName));
      response.setEntity(helper.getPhoto().get(photo));
      return response;
   }

   private Optional<Photo> findPhotoByClientId(String id, Boolean status) {
      if (!helper.isNotNull(id)) return Optional.empty();
      var tb = QPhoto.photo1;
      var condition = new BooleanBuilder();
      condition.and(tb.clientId.eq(UUID.fromString(id)));
      if (helper.isNotNull(status)) condition.and(tb.status.eq(status));
      var listBase = new ArrayList<Photo>();
      repo.findAll(condition).iterator().forEachRemaining(listBase::add);
      return listBase.stream().limit(1).findFirst();
   }
}
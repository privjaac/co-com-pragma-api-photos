package co.com.pragma.apiphotos.web.api;

import co.com.pragma.apiphotos.configuration.helper.GlobalHelper;
import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.configuration.helper.general.svc.ControllerSvc;
import co.com.pragma.apiphotos.model.dto.photo.PhotoDto;
import co.com.pragma.apiphotos.model.dto.photo.PhotoSearchDto;
import co.com.pragma.apiphotos.model.vo.PhotoVo;
import co.com.pragma.apiphotos.service.svc.PhotoSvc;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/photo")
public class PhotoCtrl implements ControllerSvc<PhotoVo, PhotoDto, PhotoSearchDto, String> {
   private final GlobalHelper helper;
   private final PhotoSvc svc;

   @Lazy
   public PhotoCtrl(GlobalHelper helper, PhotoSvc svc) {
      this.helper = helper;
      this.svc = svc;
   }

   @Override
   public Response<PhotoVo> get(String id) throws GlobalException {
      return svc.get(id);
   }

   @Override
   public Response<PhotoVo> add(@ModelAttribute PhotoDto model) throws GlobalException {
      return svc.add(model);
   }

   @Override
   public Response<PhotoVo> edit(@ModelAttribute PhotoDto model, String id) throws GlobalException {
      return svc.edit(model, id);
   }

   @Override
   public Response<PhotoVo> del(String id) throws GlobalException {
      return svc.del(id);
   }

   @Override
   public Page<PhotoVo> page(PhotoSearchDto model, Integer pageNumber, Integer pageSize) {
      return svc.page(model, helper.getDefaultPageable(pageNumber, pageSize));
   }

   @Override
   public List<PhotoVo> all(PhotoSearchDto model, Integer limit) {
      return svc.all(model, limit);
   }

   @PostMapping(value = "/add-edit")
   public Response<PhotoVo> addEdit(@RequestBody PhotoDto model) throws GlobalException {
      return svc.addEdit(model);
   }

   @GetMapping(value = "/one/client/{id}")
   public Response<PhotoVo> oneByClientId(@PathVariable String id) throws GlobalException {
      return svc.oneByClientId(id);
   }
}
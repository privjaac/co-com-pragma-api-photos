package co.com.pragma.apiphotos.service.svc;

import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.configuration.helper.general.svc.GlobalSvc;
import co.com.pragma.apiphotos.model.dto.photo.PhotoDto;
import co.com.pragma.apiphotos.model.dto.photo.PhotoSearchDto;
import co.com.pragma.apiphotos.model.vo.PhotoVo;
import co.com.pragma.apiphotos.web.exception.GlobalException;

public interface PhotoSvc extends GlobalSvc<PhotoVo, PhotoDto, PhotoSearchDto, String> {
   Response<PhotoVo> addEdit(PhotoDto model) throws GlobalException;

   Response<PhotoVo> oneByClientId(String id) throws GlobalException;
}
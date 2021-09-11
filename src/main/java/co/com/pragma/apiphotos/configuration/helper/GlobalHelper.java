package co.com.pragma.apiphotos.configuration.helper;

import co.com.pragma.apiphotos.configuration.constant.PatternConstant;
import co.com.pragma.apiphotos.configuration.helper.general.model.Response;
import co.com.pragma.apiphotos.web.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.Query;
import com.querydsl.core.QueryFactory;
import lombok.Data;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

@Data
@Service
public class GlobalHelper implements Serializable {
   private static final long serialVersionUID = 8423609393649835301L;
   private final PhotoHelper photo;

   @Lazy
   public GlobalHelper(PhotoHelper photo) {
      this.photo = photo;
   }

   public Pageable getDefaultPageable(Integer pageNumber, Integer pageSize) {
      if (pageNumber == 0) {
         pageNumber = 1;
         pageSize = Integer.MAX_VALUE;
      }
      return PageRequest.of(pageNumber - 1, pageSize);
   }

   public Pageable getDefaultPageable(Integer pageNumber, Integer pageSize, Sort sort) {
      if (pageNumber == 0) {
         pageNumber = 1;
         pageSize = Integer.MAX_VALUE;
      }
      return PageRequest.of(pageNumber - 1, pageSize, sort);
   }

   public <T> Page<T> getPageImpl(Page<T> page, Pageable pageable) {
      if (pageable == null) return page;
      var lista = page.getContent();
      var paginable = PageRequest.of(pageable.getPageNumber(), page.getSize() == Integer.MAX_VALUE ? page.getContent().isEmpty() ? 1 : page.getContent().size() : page.getSize());
      var total = page.getTotalElements();
      return new PageImpl<>(lista, paginable, total);
   }

   public String likeColumn(String columna) {
      return String.format("%%%s%%", columna);
   }

   public Boolean isNotNull(String columna) {
      return !Objects.isNull(columna) && !columna.isEmpty();
   }

   public Boolean isNotNull(Object columna) {
      return !Objects.isNull(columna);
   }

   public <T> T convertJson(String jsonText, Class<T> entidad) {
      try {
         return new ObjectMapper().readValue(jsonText, entidad);
      } catch (IOException e) {
         return null;
      }
   }

   public <T> T convertJson(Object object, Class<T> entidad) {
      return new ObjectMapper().convertValue(object, entidad);
   }

   public Boolean isNumber(String numero) {
      try {
         Integer.parseInt(numero);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }

   public double getDecimals(Double numero, Integer decimales) {
      var resultado = numero * Math.pow(10, decimales);
      resultado = Math.round(resultado);
      resultado = resultado / Math.pow(10, decimales);
      return resultado;
   }

   public Integer getNumberWeekOfYear(String fechaCompleta, String formato) {
      try {
         var calendario = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
         calendario.setTimeInMillis(1262349296 * 1000L);
         calendario.setMinimalDaysInFirstWeek(4);
         calendario.setFirstDayOfWeek(Calendar.MONDAY);
         calendario.setTime(new SimpleDateFormat(formato).parse(fechaCompleta));
         return calendario.get(Calendar.WEEK_OF_YEAR);
      } catch (ParseException e) {
         return 0;
      }
   }

   public Boolean hasPing(String ip) {
      try {
         var address = InetAddress.getByName(ip);
         return address.isReachable(1500);
      } catch (IOException e) {
         return false;
      }
   }

//   public <T > QueryFactory<T> query(){
//      return queryFactory;
//   }

   public <T, ID> T getEntity(ID id, String mensajeError, MongoRepository<T, ID> repository) throws GlobalException {
      var entidad = repository.findById(id).orElse(null);
      if (entidad != null) return entidad;
      throw new GlobalException(mensajeError);
   }


   public <T> Response<T> basicResponse(Class<T> entidadVo, String mensaje) {
      var respuesta = new Response<T>();
      return respuesta.toBuilder().flag(true).code("200").message(mensaje).build();
   }

   public <T> Response<List<T>> basicResponseList(Class<T> entidadVo, String mensaje) {
      var respuesta = new Response<List<T>>();
      return respuesta.toBuilder().flag(true).code("200").message(mensaje).build();
   }

   public Response<Map<String, Object>> basicResponseMap(String mensaje) {
      var respuesta = new Response<Map<String, Object>>();
      return respuesta.toBuilder().flag(true).code("200").message(mensaje).build();
   }

   public <T> Response<Page<T>> basicResponsePage(Class<T> entidadVo, String mensaje) {
      var respuesta = new Response<Page<T>>();
      return respuesta.toBuilder().flag(true).code("200").message(mensaje).build();
   }

   public String generateZeros(Integer numeroRepeticiones, Integer valor) {
      Function<Integer, String> repeatCeros = "0"::repeat;
      var ceros = repeatCeros.apply(numeroRepeticiones);
      var valorId = valor.toString();
      var random = ceros.substring(0, (ceros.length() - valorId.length()));
      return String.format("%s%s", random, valorId);
   }

   public String extractText(String textoInicial, String textoFinal, String texto) {
      var pattern = Pattern.compile(textoInicial + "(.+?)" + textoFinal, Pattern.DOTALL);
      var matcher = pattern.matcher(texto);
      return matcher.find() ? matcher.group(1).trim() : "";
   }

   public SSLContext initUnsecuredTSLContext() throws NoSuchAlgorithmException, KeyManagementException {
      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
         @Override
         public X509Certificate[] getAcceptedIssuers() {
            return null;
         }

         @Override
         public void checkClientTrusted(X509Certificate[] certs, String authType) {
         }

         @Override
         public void checkServerTrusted(X509Certificate[] certs, String authType) {
         }
      }};
      var context = SSLContext.getInstance("TLS");
      context.init(null, trustAllCerts, null);
      HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
      var allHostsValid = new NoopHostnameVerifier();
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      return context;
   }

   public void sleep(long milisegundos) {
      try {
         Thread.sleep(milisegundos);
      } catch (InterruptedException ignored) {
      }
   }

   public String noSpecialCaracteres(String texto) {
      texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
      texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
      return texto;
   }

   public <T> T emitNull(Object objectOne, T objectTwo) {
      if (Objects.isNull(objectOne)) return null;
      return objectTwo;
   }

   public String emitNullDateTimePE(LocalDateTime dateTime) {
      if (Objects.isNull(dateTime)) return null;
      return dateTime.format(DateTimeFormatter.ofPattern(PatternConstant.PATTERN_DATE_DEFAULT_PE));
   }

   public String emitNullDateTimeIN(LocalDateTime dateTime) {
      if (Objects.isNull(dateTime)) return null;
      return dateTime.format(DateTimeFormatter.ofPattern(PatternConstant.PATTERN_DATE_DEFAULT_IN));
   }

   public String emitNullTime(LocalTime time) {
      if (Objects.isNull(time)) return null;
      return time.format(DateTimeFormatter.ofPattern(PatternConstant.PATTERN_TIME));
   }

   public byte[] getBytesFromFile(File file) {
      try {
         return Files.readAllBytes(file.toPath());
      } catch (IOException e) {
         return new byte[]{};
      }
   }
}

package nu.peg.svmeal.infrastructure.util;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class HttpUtil {

  public static HttpEntity<MultiValueMap<String, String>> getPostFormData(
      Map<String, Object> formData) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    formData.forEach((k, v) -> map.add(k, v.toString()));

    return new HttpEntity<>(map, headers);
  }
}

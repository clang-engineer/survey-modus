package com.clangengineer.surveymodus.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class KakaoAlertServiceTest {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";
//
//    public static String authToken;
//
//    public boolean getKakaoAuthToken(String code)  {
//        HttpHeaders header = new HttpHeaders();
//        String accessToken = "";
//        String refrashToken = "";
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//
//        header.set("Content-Type", APP_TYPE_URL_ENCODED);
//
//        parameters.add("code", code);
//        parameters.add("grant_type", "authorization_code");
//        parameters.add("client_id", "your client id");
//        parameters.add("redirect_url", "http://localhost:88");
//        parameters.add("client_secret", "your client secret");
//
//        HttpEntity<?> requestEntity = httpClientEntity(header, parameters);
//
//        ResponseEntity<String> response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
//        JSONObject jsonData = new JSONObject(response.getBody());
//        accessToken = jsonData.get("access_token").toString();
//        refrashToken = jsonData.get("refresh_token").toString();
//        if(accessToken.isEmpty() || refrashToken.isEmpty()) {
//            logger.debug("토큰발급에 실패했습니다.");
//            return false;
//        }else {
//            authToken = accessToken;
//            return true;
//        }
//    }

    @Test
    fun getKakaoAuthToken() {

//        curl -X POST "https://{base_url}/v2/oauth/token" \
//        -H  "accept: */*" \
//        -H  "Authorization: Basic {clientID} {clientSecret}" \
//        -H  "Content-Type: application/x-www-form-urlencoded" \
//        -d  "grant_type=client_credentials"

        val KAKAO_AUTH_URL = "https://bizmsg-web.kakaoenterprise.com/v2/oauth/token"
        val CLIENT_ID = "1184478"
        val CLIENT_SECRET = "iy37Ikasef8rnP3VYEMEdAaaiuwcHwjF"

        val request = HttpRequest.newBuilder()
            .uri(URI.create(KAKAO_AUTH_URL))
            .header("accept", "*/*")
            .header("Authorization", "Basic $CLIENT_ID $CLIENT_SECRET")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
            .build()

        val httpClient = HttpClient.newHttpClient()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertThat(response.statusCode()).isEqualTo(200)
    }
}

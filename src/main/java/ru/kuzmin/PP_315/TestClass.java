package ru.kuzmin.PP_315;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.List;


@Component
public class TestClass {

    private final RestTemplate restTemplate;

    @Autowired
    public TestClass(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @PostConstruct
    public void init() {

        String url = "http://94.198.50.185:7081/api/users/";
        User user = new User(3L, "James", "Brown", (byte) 10);
        HttpHeaders headers = createHeader(url);
        String firstPartOfCode = postRequest(url, headers, user);
        user.setName("Thomas");
        user.setLastName("Shelby");
        String secondPartOfCode = putRequest(url, headers, user);
        String thirdPartOfCode = deleteRequest(url, headers, 3);
        System.out.println(firstPartOfCode + secondPartOfCode + thirdPartOfCode);

    }

    private HttpHeaders createHeader(String url) {
        ResponseEntity<String> rateResponse = restTemplate.exchange(url,
                HttpMethod.GET, null, String.class);
        HttpHeaders ResponseHeaders = rateResponse.getHeaders();
        String cookieHeader = ResponseHeaders.get("Set-Cookie").get(0).split(";")[0];
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Cookie", cookieHeader);
        return headers;
    }

    private String postRequest(String url, HttpHeaders headers, User user) {
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
    }

    private String putRequest(String url, HttpHeaders headers, User userToUpdate) {
        HttpEntity<User> entity = new HttpEntity<>(userToUpdate, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();

    }

    private String deleteRequest(String url, HttpHeaders headers, int id) {
        HttpEntity<User> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url + id, HttpMethod.DELETE, entity, String.class).getBody();
    }


}

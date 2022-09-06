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
        String cookieHeader = getCookieHeader(url);
        String firstPartOfCode = postRequest(url, cookieHeader, user);
        user.setName("Thomas");
        user.setLastName("Shelby");
        String secondPartOfCode = putRequest(url, cookieHeader, user);
        String thirdPartOfCode = deleteRequest(url, cookieHeader, 3);
        System.out.println(firstPartOfCode + secondPartOfCode + thirdPartOfCode);

    }

    public String getCookieHeader(String url) {
        ResponseEntity<String> rateResponse = restTemplate.exchange(url,
                HttpMethod.GET, null, String.class);
        HttpHeaders headers = rateResponse.getHeaders();
        return headers.get("Set-Cookie").get(0).split(";")[0];
    }

    public String postRequest(String url, String cookieHeader, User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Cookie", cookieHeader);
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        return restTemplate.exchange(url,
                HttpMethod.POST, entity, String.class).getBody();
    }

    public String putRequest(String url, String cookieHeader, User userToUpdate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Cookie", cookieHeader);
        HttpEntity<User> entity = new HttpEntity<>(userToUpdate, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();

    }

    public String deleteRequest(String url, String cookieHeader, int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Cookie", cookieHeader);
        HttpEntity<User> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url + id, HttpMethod.DELETE, entity, String.class).getBody();
    }
}

import model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class Main {
    public static void main(String[] args) {
        String baseUrl = "http://94.198.50.185:7081/api/users";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); // <- ??? ?????!

        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl, String.class);
        String sessionId = getResponse.getHeaders().getFirst("Set-Cookie").split(";")[0];

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 25);

        HttpEntity<User> postRequest = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, postRequest, String.class);
        String codePart1 = postResponse.getBody();

        User updatedUser = new User();
        updatedUser.setId(3L);
        updatedUser.setName("Thomas");
        updatedUser.setLastName("Shelby");
        updatedUser.setAge((byte) 25);

        HttpEntity<User> putRequest = new HttpEntity<>(updatedUser, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(baseUrl, HttpMethod.PUT, putRequest, String.class);
        String codePart2 = putResponse.getBody();

        HttpEntity<String> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                baseUrl + "/3",
                HttpMethod.DELETE,
                deleteRequest,
                String.class
        );
        String codePart3 = deleteResponse.getBody();

        System.out.println("Код вот он: " + codePart1 + codePart2 + codePart3);
    }
}

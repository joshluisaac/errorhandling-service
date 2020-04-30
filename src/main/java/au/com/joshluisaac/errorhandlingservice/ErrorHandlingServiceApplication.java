package au.com.joshluisaac.errorhandlingservice;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ErrorHandlingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ErrorHandlingServiceApplication.class, args);
  }
}

@RestController
class CustomController {

  @GetMapping("/names")
  public Mono<ResponseEntity<List<Person>>> getNames() {
    var person1 = new Person("Josh", 10);
    var person2 = new Person("Sam", 20);
    return Mono.just(new ResponseEntity<List<Person>>(List.of(person1, person2), HttpStatus.OK));
  }
}

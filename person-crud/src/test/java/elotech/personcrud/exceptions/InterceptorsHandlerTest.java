package elotech.personcrud.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Assertions;

@SpringBootTest
public class InterceptorsHandlerTest {
    private static final String MESSAGE = "message";

    @Test
    public void objectNotFoundTest() {
        InterceptorsHandler interceptorsHandler = new InterceptorsHandler();
        ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException(MESSAGE);
        ResponseEntity<ErrorResponse> response = interceptorsHandler.objectNotFoundException(objectNotFoundException);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
        Assertions.assertTrue(response.getBody().getMessage().equals(MESSAGE));
    }

}

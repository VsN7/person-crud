package elotech.personcrud.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LegalIdentifierValidateTest {

    @Autowired
    private LegalIdentifierValidateService service;

    @Test
    public void validTest(){
        Assertions.assertTrue(this.service.valid("056.647.421-27".replaceAll("[^0-9]", "")));
    }

    @Test
    public void invalidTest(){
        Assertions.assertFalse(this.service.valid("056.647.421-90".replaceAll("[^0-9]", "")));
        Assertions.assertFalse(this.service.valid("056.647/sa421-90".replaceAll("[^0-9]", "")));
        Assertions.assertFalse(this.service.valid("056.647.4121-90".replaceAll("[^0-9]", "")));
        Assertions.assertFalse(this.service.valid("056.647.421-27"));
    }

}

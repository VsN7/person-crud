package elotech.personcrud.controller;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.model.Person;
import elotech.personcrud.service.PersonService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class PersonControllerTest {

    @Autowired
    private PersonController controller;
    @MockBean
    private PersonService personService;

    private EasyRandom easyRandom = new EasyRandom();

    @Test
    public void saveTest(){
        PersonDTO personDTO = this.easyRandom.nextObject(PersonDTO.class);
        Mockito.when(this.personService.save(Mockito.any())).thenReturn(personDTO);
        ResponseEntity<PersonDTO> response = this.controller.save(personDTO);
        Assertions.assertNotNull(response);
    }

    @Test
    public void updateTest(){
        Mockito.doNothing().when(this.personService).update(Mockito.any());
        ResponseEntity<Void> response = this.controller.update(Mockito.any());
        Assertions.assertNotNull(response);
    }

    @Test
    public void deleteTest(){
        Mockito.doNothing().when(this.personService).delete(Mockito.any());
        ResponseEntity<Void> response = this.controller.delete(Mockito.any());
        Assertions.assertNotNull(response);
    }

    @Test
    public void findAllTest(){
        List<Person> personList = Arrays.asList(this.easyRandom.nextObject(Person.class));
        Page<Person> personPage = new PageImpl<>(personList);
        Mockito.when(this.personService.findAll(0,10,"")).thenReturn(personPage);
        ResponseEntity<Page<Person>> response = this.controller.findAll(0,10,"");
        Assertions.assertNotNull(response);
    }

    @Test
    public void getByIdTest(){
        PersonDTO personDTO = this.easyRandom.nextObject(PersonDTO.class);
        personDTO.setId(1L);
        Mockito.when(this.personService.getById(1L)).thenReturn(personDTO);
        ResponseEntity<PersonDTO> response = this.controller.getById(1L);
        Assertions.assertNotNull(response);
    }
}

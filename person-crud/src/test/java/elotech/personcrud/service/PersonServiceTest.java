package elotech.personcrud.service;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import elotech.personcrud.repository.PersonRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService service;
    @MockBean
    private PersonRepository repository;

    @MockBean
    private ContactService contactService;
    @MockBean
    private LegalIdentifierValidateService legalIdentifierValidateService;

    private EasyRandom easyRandom = new EasyRandom();

    @Test
    public void saveTest(){
        List<Contact> contactList = Arrays.asList(easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, "vitor", new Date(), "056.647.421-27", contactList);
        Mockito.when(legalIdentifierValidateService.valid("056.647.421-27".replaceAll("[^0-9]", ""))).thenReturn(true);
        Mockito.when(repository.save(Mockito.any())).thenReturn(payload.toEntity());

        Mockito.doNothing().when(contactService).prepareSaveContact(payload, payload.toEntity());
        PersonDTO response = service.save(payload);
        Assertions.assertNotNull(response);
    }

    @Test()
    public void saveLegalIdentifierInvalidTest(){
        List<Contact> contactList = Arrays.asList(this.easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, "vitor", new Date(), "056.647.4281-27", contactList);
        Mockito.when(this.legalIdentifierValidateService.valid("056.647.4281-27".replaceAll("[^0-9]", ""))).thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.save(payload);
        });
    }

    @Test()
    public void saveBirthDateInvalidTest(){
        List<Contact> contactList = Arrays.asList(this.easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, "vitor", new Date(2030,10,2), "056.647.421-27", contactList);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.save(payload);
        });
    }

    @Test()
    public void saveRequiredInputNameTest(){
        List<Contact> contactList = Arrays.asList(this.easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, null, new Date(), "056.647.421-27", contactList);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.save(payload);
        });
    }

    @Test()
    public void saveRequiredInputBirthDateTest(){
        List<Contact> contactList = Arrays.asList(this.easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, "null", null, "056.647.421-27", contactList);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.save(payload);
        });
    }

    @Test()
    public void saveRequiredInputLegalIdentifiereTest(){
        List<Contact> contactList = Arrays.asList(this.easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, "null", new Date(), null, contactList);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.save(payload);
        });
    }

    @Test()
    public void saveContactListEmptyTest(){
        List<Contact> contactList = Arrays.asList(easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(null);
        PersonDTO payload = this.createMockPerson(null, "meu nome", new Date(), "056.647.421-27", new ArrayList<>());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.save(payload);
        });
    }

    @Test
    public void updateTest(){
        List<Contact> contactList = Arrays.asList(easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(1L);
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", contactList);
        Mockito.when(this.legalIdentifierValidateService.valid("056.647.421-27".replaceAll("[^0-9]", ""))).thenReturn(true);
        Mockito.when(this.repository.save(Mockito.any())).thenReturn(payload.toEntity());
        Mockito.when(this.repository.getById(1L)).thenReturn(payload.toEntity());
        Mockito.doNothing().when(this.contactService).updateOrAddOrRemoveContact(payload);
        this.service.update(payload);
    }

    @Test
    public void updateIdNullTest(){
        List<Contact> contactList = Arrays.asList(this.easyRandom.nextObject(Contact.class));
        contactList.get(0).setId(1L);
        PersonDTO payload = this.createMockPerson(null, "vitor", new Date(), "056.647.421-27", contactList);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.update(payload);
        });
    }

    @Test
    public void deleteTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Mockito.when(this.repository.getById(1L)).thenReturn(payload.toEntity());
        Mockito.doNothing().when(this.contactService).deleteByPerson(payload.toEntity());
        Mockito.doNothing().when(this.repository).delete(payload.toEntity());
        this.service.delete(payload);
    }

    private PersonDTO createMockPerson(Long id, String name, Date birthDate, String legalIdentifier, List<Contact> contactList){
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setDateBirth(birthDate);
        person.setLegalIdentifier(legalIdentifier);
        person.setContactList(contactList);
        return PersonDTO.toDTO(person);
    }


}

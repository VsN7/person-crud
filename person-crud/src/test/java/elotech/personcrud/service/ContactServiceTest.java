package elotech.personcrud.service;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import elotech.personcrud.repository.ContactRepository;
import elotech.personcrud.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ContactServiceTest {

    @Autowired
    private ContactService service;
    @MockBean
    private ContactRepository repository;

    private EasyRandom easyRandom = new EasyRandom();

    @Test
    public void saveTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Contact contact = this.createMockContact(null,"vitor","email@email.com","18981406885",payload.toEntity());
        payload.setContactList(Arrays.asList(contact));
        contact.setPerson(payload.toEntity());
        Mockito.when(this.repository.save(Mockito.any())).thenReturn(contact);
        this.service.prepareSaveContact(payload, payload.toEntity());
    }

    @Test
    public void updateOrAddOrRemoveContactTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Contact contact = this.createMockContact(null,"vitor","email@email.com","18981406885",payload.toEntity());
        payload.setContactList(Arrays.asList(contact));
        List<Contact> contactList = Arrays.asList(contact);
        Mockito.when(this.repository.findByPerson(payload.toEntity())).thenReturn(contactList);
        this.service.updateOrAddOrRemoveContact(payload);
    }

    @Test
    public void removeTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Mockito.doNothing().when(this.repository).deleteByPerson(payload.toEntity());
        this.service.deleteByPerson(payload.toEntity());
    }

    @Test
    public void getByIdTest(){
        Contact contact = this.createMockContact(1L,"vitor","email@email.com","18981406885", null);
        Mockito.when(this.repository.getById(1L)).thenReturn(contact);
        Contact response =this.service.getById(1L);
        Assertions.assertNotNull(response);
    }
    public Contact getById(Long id) {
        try {
            return repository.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("contato não encontrado");
        }
    }

    @Test
    public void saveEmailInvalidTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Contact contact = this.createMockContact(null,"vitor","emailemail.com","18981406885",payload.toEntity());
        payload.setContactList(Arrays.asList(contact));
        contact.setPerson(payload.toEntity());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.prepareSaveContact(payload, payload.toEntity());
        });
    }

    @Test
    public void saveRequiredInputNameTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Contact contact = this.createMockContact(null,null,"email@email.com","18981406885",payload.toEntity());
        payload.setContactList(Arrays.asList(contact));
        contact.setPerson(payload.toEntity());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.prepareSaveContact(payload, payload.toEntity());
        });
    }

    @Test
    public void saveRequiredInputEmailTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Contact contact = this.createMockContact(null,"null",null,"18981406885",payload.toEntity());
        payload.setContactList(Arrays.asList(contact));
        contact.setPerson(payload.toEntity());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.prepareSaveContact(payload, payload.toEntity());
        });
    }

    @Test
    public void saveRequiredInputTelephoneTest(){
        PersonDTO payload = this.createMockPerson(1L, "vitor", new Date(), "056.647.421-27", new ArrayList<>());
        Contact contact = this.createMockContact(null,"null","null@null.com",null,payload.toEntity());
        payload.setContactList(Arrays.asList(contact));
        contact.setPerson(payload.toEntity());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            this.service.prepareSaveContact(payload, payload.toEntity());
        });
    }

    private Contact createMockContact(Long id, String name, String email, String telephone, Person person){
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(name);
        contact.setEmail(email);
        contact.setPerson(person);
        contact.setTelephone(telephone);
        return contact;
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

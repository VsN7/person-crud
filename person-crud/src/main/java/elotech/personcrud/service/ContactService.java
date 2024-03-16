package elotech.personcrud.service;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import elotech.personcrud.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public void saveContact(PersonDTO payload){
        for (Contact contact: payload.getContactList()){
            contact.setPerson(payload.toEntity());
            this.payloadContactValidate(contact);
        }
        repository.saveAll(payload.getContactList());
    }

    private void payloadContactValidate(Contact contact) throws ObjectNotFoundException {
        if(Objects.isNull(contact.getTelephone())){
            throw new ObjectNotFoundException("telefone do contato é obrigatório");
        }
        if(Objects.isNull(contact.getEmail())){
            throw new ObjectNotFoundException("e-mail do contato é obrigatório");
        }
    }

}

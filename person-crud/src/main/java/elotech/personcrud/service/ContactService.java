package elotech.personcrud.service;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import elotech.personcrud.repository.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Transactional
@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public void prepareSaveContact(PersonDTO payload, Person person){
        for (Contact contact: payload.getContactList()){
            this.payloadContactValidate(contact);
            contact.setPerson(person);
            this.repository.save(contact);
        }
    }

    public void updateOrAddOrRemoveContact(PersonDTO payload){
        this.updateAll(payload.getContactList());
        List<Contact> contactsOld = this.repository.findByPerson(payload.toEntity());
        this.removeAllContract(payload.getContactList(), contactsOld);
        this.saveAllContract(payload, contactsOld);
    }

    public void deleteByPerson(Person person){
        this.repository.deleteByPerson(person);
    }

    public Contact getById(Long id) {
        try {
            return repository.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("contato não encontrado");
        }
    }

    private void updateAll(List<Contact> contactList){
        List<Contact> contractUpdateList = new ArrayList<>();

        for (Contact contact: contactList){
            if(Objects.nonNull(contact.getId())) {
                this.payloadContactValidate(contact);
                this.thereIsContact(contact);
                contractUpdateList.add(contact);
            }
        }
        if(!contractUpdateList.isEmpty()) {
            this.repository.saveAll(contractUpdateList);
        }

    }

    private void saveAllContract(PersonDTO payload, List<Contact> contractListOld){
        List<Contact> contactsAdd = new ArrayList<>();
        for (Contact contact: payload.getContactList()) {
            if (!contractListOld.contains(contact)) {
                this.payloadContactValidate(contact);
                Person person = new Person();
                person.setId(payload.getId());
                contact.setPerson(person);
                contactsAdd.add(contact);
            }
        }
        if(!contactsAdd.isEmpty()){
            repository.saveAll(contactsAdd);
        }
    }

    private void removeAllContract(List<Contact> contactListNew, List<Contact> contractListOld){
        List<Contact> contactsRemove = new ArrayList<>();
        for (Contact contact : contractListOld) {
            if (!contactListNew.contains(contact)) {
                contactsRemove.add(contact);
            }
        }
        if(!contactsRemove.isEmpty()){
            repository.deleteAll(contactsRemove);
        }
    }

    private void thereIsContact(Contact contact){
        this.getById(contact.getId());
    }

    private void emailValidate(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(regex, email)) {
            throw new ObjectNotFoundException("O email: " + email + " do contato possui uma sintaxe inválida.");
        }
    }

    private void payloadContactValidate(Contact contact) throws ObjectNotFoundException {
        this.payloadRequiredFields(contact);
        this.emailValidate(contact.getEmail());
    }

    private void payloadRequiredFields(Contact contact) {
        if(Objects.isNull(contact.getTelephone())){
            throw new ObjectNotFoundException("telefone do contato é obrigatório");
        }
        if(Objects.isNull(contact.getEmail())){
            throw new ObjectNotFoundException("e-mail do contato é obrigatório");
        }
        if(Objects.isNull(contact.getName())){
            throw new ObjectNotFoundException("nome do contato é obrigatório");
        }
    }

}

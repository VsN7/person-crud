package elotech.personcrud.service;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import elotech.personcrud.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private ContactService contactService;
    @Autowired
    private LegalIdentifierValidateService legalIdentifierValidateService;

    public PersonDTO save(PersonDTO payload) throws ObjectNotFoundException {
        this.payloadPersonValidate(payload);
        payload.setLegalIdentifier(payload.getLegalIdentifier().replaceAll("[^0-9]", ""));
        Person person = repository.save(payload.toEntity());
        this.contactService.prepareSaveContact(payload, person);
        payload.setId(person.getId());
        return payload;
    }

    public void update(PersonDTO payload) throws ObjectNotFoundException {
        this.idValidate(payload.getId());
        this.dateBirthValidate(payload.getDateBirth());
        this.contactValidate(payload.getContactList());
        this.legalIdentifierValidate(payload.getLegalIdentifier());
        this.thereIsPerson(payload.toEntity());
        payload.setLegalIdentifier(payload.getLegalIdentifier().replaceAll("[^0-9]", ""));
        repository.save(payload.toEntity());
        this.contactService.updateOrAddOrRemoveContact(payload);
    }

    public void delete(PersonDTO payload) throws ObjectNotFoundException {
        this.idValidate(payload.getId());
        this.thereIsPerson(payload.toEntity());
        this.contactService.deleteByPerson(payload.toEntity());
        repository.delete(payload.toEntity());
    }

    public PersonDTO getById(Long id) {
        try {
            return PersonDTO.toDTO(repository.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("pessoa não encontrada");
        }
    }

    public Page<Person> findAll(int page, int size, String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return repository.findAll(pageable);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("pessoa não encontrada");
        }
    }

    private void idValidate(Long id){
        if(Objects.isNull(id)){
            throw new ObjectNotFoundException("necessário informar o id da pessoa");
        }
    }

    private void payloadPersonValidate(PersonDTO payload) throws ObjectNotFoundException {
        this.payloadRequiredFields(payload);
        this.contactValidate(payload.getContactList());
        this.dateBirthValidate(payload.getDateBirth());
        this.legalIdentifierValidate(payload.getLegalIdentifier());
        this.thereIsPersonForLegalIdentifier(payload);
    }

    private void payloadRequiredFields(PersonDTO payload){
        if(Objects.isNull(payload)){
            throw new ObjectNotFoundException("necessário informar uma pessoa");
        }
        if(Objects.isNull(payload.getName())){
            throw new ObjectNotFoundException("nome é obrigatório");
        }
        if(Objects.isNull(payload.getLegalIdentifier())){
            throw new ObjectNotFoundException("CPF é obrigatório");
        }
        if(Objects.isNull(payload.getDateBirth())){
            throw new ObjectNotFoundException("data de nascimento é obrigatório");
        }
    }

    private void legalIdentifierValidate(String legalIdentifier){
        if (!this.legalIdentifierValidateService.valid(legalIdentifier.replaceAll("[^0-9]", ""))){
            throw new ObjectNotFoundException("CPF invalido.");
        }
    }

    private void contactValidate(List<Contact> contactList) {
        if (Objects.isNull(contactList) || contactList.isEmpty()) {
            throw new ObjectNotFoundException("A pessoa deve possuir ao menos um contato.");
        }
    }

    private void dateBirthValidate(Date dateBirth) {
        if (dateBirth.after(new Date())) {
            throw new ObjectNotFoundException("A data de nascimento não pode ser uma data futura.");
        }
    }

    private void thereIsPerson(Person person){
        this.getById(person.getId());
    }

    private void thereIsPersonForLegalIdentifier(PersonDTO personDTO){
        Person person = repository.getByLegalIdentifier(personDTO.getLegalIdentifier());
        if(Objects.nonNull(person)) {
            throw new ObjectNotFoundException("CPF: " + person.getLegalIdentifier() + " Já cadastrado na base");
        }
    }

}

package elotech.personcrud.service;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import elotech.personcrud.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Objects;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private ContactService contactService;

    public PersonDTO save(PersonDTO payload) throws ObjectNotFoundException {
        this.payloadPersonValidate(payload);
        this.thereIsPersonForLegalIdentifier(payload);
        if(Objects.isNull(payload.getContactList()) && payload.getContactList().isEmpty()){
            throw new ObjectNotFoundException("necessário informar pelo menos 1 contato");
        }
        Person person = repository.save(payload.toEntity());
        return PersonDTO.toDTO(person);
    }

    public void update(PersonDTO payload) throws ObjectNotFoundException {
        this.payloadPersonValidate(payload);
        if(Objects.isNull(payload.getId())){
            throw new ObjectNotFoundException("necessário informar o id da pessoa");
        }
        this.thereIsPersonForLegalIdentifier(payload);
        repository.save(payload.toEntity());
    }

    public void delete(PersonDTO payload) throws ObjectNotFoundException {
        if(Objects.isNull(payload.getId())){
            throw new ObjectNotFoundException("necessário informar o id da pessoa");
        }
        this.thereIsPerson(payload.toEntity());
        repository.delete(payload.toEntity());
    }

    private void payloadPersonValidate(PersonDTO payload) throws ObjectNotFoundException {
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

    private void thereIsPerson(Person person){
        this.getById(person.getId());
    }

    private void thereIsPersonForLegalIdentifier(PersonDTO personDTO){
        Person person = repository.getByLegalIdentifier(personDTO.getLegalIdentifier());
        if(Objects.nonNull(person)) {
            throw new ObjectNotFoundException("CPF: " + person.getLegalIdentifier() + " Já cadastrado na base");
        }
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

}

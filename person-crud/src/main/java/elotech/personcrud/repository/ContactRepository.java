package elotech.personcrud.repository;

import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    void deleteByPerson(Person person);

    List<Contact> findByPerson(Person person);
}

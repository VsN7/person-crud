package elotech.personcrud.repository;

import elotech.personcrud.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person getByLegalIdentifier(String legalIdentifier);
}

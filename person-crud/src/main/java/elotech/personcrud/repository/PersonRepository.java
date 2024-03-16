package elotech.personcrud.repository;

import elotech.personcrud.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person getByLegalIdentifier(String legalIdentifier);

    @Query(value = "SELECT p.\"last_value\"+1  FROM person_id_seq p", nativeQuery = true)
    Long getNextPersonId();
}

package elotech.personcrud.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "PersonSeq", sequenceName = "person_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PersonSeq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "legal_identifier")
    private String legalIdentifier;

    @Column(name = "date_birth")
    private Date dateBirth;

    @OneToMany(targetEntity = Contact.class, mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<Contact> contactList;
}

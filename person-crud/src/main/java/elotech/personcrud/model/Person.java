package elotech.personcrud.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "person")
    private List<Contact> contactList = new ArrayList<>();
}

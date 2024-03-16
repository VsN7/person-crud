package elotech.personcrud.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
public class Contact {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "PersonSeq", sequenceName = "person_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PersonSeq")
    private Long id;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @JoinColumn(name = "person_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Person person;


}

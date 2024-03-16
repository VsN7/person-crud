package elotech.personcrud.dto;

import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
public class PersonDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String legalIdentifier;
    private Date dateBirth;
    private List<Contact> contactList;

    public Person toEntity(){
        Person person = new Person();
        person.setId(this.id);
        person.setName(this.name);
        person.setLegalIdentifier(this.legalIdentifier);
        person.setDateBirth(this.dateBirth);
        person.setContactList(this.contactList);
        return person;
    }

    public static PersonDTO toDTO( Person person ) {

        if(Objects.isNull(person)) {
            return null;
        }

        return PersonDTO.builder()
                .id( person.getId() )
                .name( person.getName() )
                .legalIdentifier( person.getLegalIdentifier() )
                .dateBirth(person.getDateBirth())
                .contactList(person.getContactList())
                .build();
    }
}

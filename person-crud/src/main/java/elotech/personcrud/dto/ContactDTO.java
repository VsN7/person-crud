package elotech.personcrud.dto;

import elotech.personcrud.model.Contact;
import elotech.personcrud.model.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class ContactDTO {
    private Long id;
    private String telephone;
    private String email;
    private Person person;

    public static ContactDTO toDTO( Contact contact ) {

        if(Objects.isNull(contact)) {
            return null;
        }

        return ContactDTO.builder()
                .id( contact.getId() )
                .telephone( contact.getTelephone() )
                .email( contact.getEmail() )
                .person(contact.getPerson())
                .build();
    }

}

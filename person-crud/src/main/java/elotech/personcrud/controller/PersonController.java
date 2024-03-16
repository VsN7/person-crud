package elotech.personcrud.controller;

import elotech.personcrud.dto.PersonDTO;
import elotech.personcrud.exceptions.ObjectNotFoundException;
import elotech.personcrud.model.Person;
import elotech.personcrud.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> save(@RequestBody PersonDTO payload) throws ObjectNotFoundException {
        PersonDTO personDTO = this.service.save(payload);
        return ResponseEntity.ok().body(personDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void>  update(@RequestBody PersonDTO payload) throws ObjectNotFoundException {
        this.service.update(payload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void>  delete(@RequestBody PersonDTO payload) throws ObjectNotFoundException {
        this.service.delete(payload);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<Person>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<Person> personList = this.service.findAll(page, size, sortBy);
        return ResponseEntity.ok().body(personList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getById(@PathVariable Long id){
        PersonDTO personDTO = this.service.getById(id);
        return ResponseEntity.ok().body(personDTO);
    }

}

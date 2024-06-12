package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO create(@RequestBody ContactCreateDTO contactDTO) {
        var contact = toEntity(contactDTO);
        contactRepository.save(contact);
        var dto = toDTO(contact);
        return dto;
    }

    private ContactDTO toDTO(Contact contact) {
        var dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setPhone(contact.getPhone());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setCreatedAt(contact.getCreatedAt());
        dto.setUpdatedAt(contact.getUpdatedAt());
        return dto;
    }

    private Contact toEntity(ContactCreateDTO contactDTO) {
        var contact = new Contact();
        contact.setPhone(contactDTO.getPhone());
        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        return contact;
    }
    // END
}

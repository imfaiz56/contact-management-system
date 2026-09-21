package com.faiz.contactmanagement.service;

import com.faiz.contactmanagement.dto.ContactRequest;
import com.faiz.contactmanagement.dto.LinkRequest;
import com.faiz.contactmanagement.exception.ContactNotFoundException;
import com.faiz.contactmanagement.exception.DuplicateContactException;
import com.faiz.contactmanagement.model.Contact;
import com.faiz.contactmanagement.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(ContactRequest request) {
        contactRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            throw new DuplicateContactException("A contact with this email already exists");
        });
        contactRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(c -> {
            throw new DuplicateContactException("A contact with this phone number already exists");
        });

        Contact contact = new Contact(request.getFullName(), request.getPhoneNumber(), request.getEmail());
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));
    }

    public Contact updateContact(Long id, ContactRequest request) {
        Contact existing = getContactById(id);

        contactRepository.findByEmail(request.getEmail())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new DuplicateContactException("Another contact already uses this email");
                });
        contactRepository.findByPhoneNumber(request.getPhoneNumber())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new DuplicateContactException("Another contact already uses this phone number");
                });

        existing.setFullName(request.getFullName());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setEmail(request.getEmail());
        return contactRepository.save(existing);
    }

    public void deleteContact(Long id) {
        Contact existing = getContactById(id);
        contactRepository.delete(existing);
    }

    public List<Contact> search(String query) {
        List<Contact> byName = contactRepository.findByFullNameContainingIgnoreCase(query);
        if (!byName.isEmpty()) return byName;

        List<Contact> byPhone = contactRepository.findByPhoneNumberContaining(query);
        if (!byPhone.isEmpty()) return byPhone;

        return contactRepository.findByEmailContainingIgnoreCase(query);
    }

    // ---- "Six Degrees" relationship linking feature ----

    public Contact linkContact(Long contactId, LinkRequest request) {
        Contact contact = getContactById(contactId);
        Contact relatedTo = getContactById(request.getRelatedToId());

        if (contact.getId().equals(relatedTo.getId())) {
            throw new IllegalArgumentException("A contact cannot be linked to itself");
        }

        contact.setRelatedTo(relatedTo);
        contact.setRelationshipLabel(request.getRelationshipLabel());
        return contactRepository.save(contact);
    }

    public Contact unlinkContact(Long contactId) {
        Contact contact = getContactById(contactId);
        contact.setRelatedTo(null);
        contact.setRelationshipLabel(null);
        return contactRepository.save(contact);
    }

    // Returns all root contacts (no manager/parent) with their nested linkedContacts populated,
    // effectively giving a full relationship tree/forest.
    public List<Contact> getRelationshipTree() {
        return contactRepository.findByRelatedToIsNull();
    }
}

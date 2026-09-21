package com.faiz.contactmanagement.controller;

import com.faiz.contactmanagement.dto.ContactRequest;
import com.faiz.contactmanagement.dto.LinkRequest;
import com.faiz.contactmanagement.model.Contact;
import com.faiz.contactmanagement.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@Valid @RequestBody ContactRequest request) {
        Contact created = contactService.createContact(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @Valid @RequestBody ContactRequest request) {
        return ResponseEntity.ok(contactService.updateContact(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/contacts/search?query=faiz  -> matches name, then phone, then email
    @GetMapping("/search")
    public ResponseEntity<List<Contact>> search(@RequestParam String query) {
        return ResponseEntity.ok(contactService.search(query));
    }

    // ---- Relationship linking ("Six Degrees" bonus feature) ----

    // POST /api/contacts/5/link  { "relatedToId": 2, "relationshipLabel": "Manager" }
    // Example: "John is the manager of Sarah" -> POST /api/contacts/{sarahId}/link { relatedToId: johnId, relationshipLabel: "Manager" }
    @PostMapping("/{id}/link")
    public ResponseEntity<Contact> linkContact(@PathVariable Long id, @Valid @RequestBody LinkRequest request) {
        return ResponseEntity.ok(contactService.linkContact(id, request));
    }

    @DeleteMapping("/{id}/link")
    public ResponseEntity<Contact> unlinkContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.unlinkContact(id));
    }

    // GET /api/contacts/tree -> returns root contacts with nested linkedContacts (the visual relationship tree)
    @GetMapping("/tree")
    public ResponseEntity<List<Contact>> getRelationshipTree() {
        return ResponseEntity.ok(contactService.getRelationshipTree());
    }
}

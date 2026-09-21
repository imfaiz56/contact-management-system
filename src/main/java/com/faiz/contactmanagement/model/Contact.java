package com.faiz.contactmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Enter a valid phone number")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    // Bonus "Six Degrees" feature: a contact can report to / be linked under another contact.
    // e.g. "John is the manager of Sarah" -> Sarah.relatedTo = John
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "related_to_id")
    @JsonIgnoreProperties({"relatedTo", "linkedContacts"})
    private Contact relatedTo;

    @OneToMany(mappedBy = "relatedTo")
    @JsonIgnoreProperties({"relatedTo", "linkedContacts"})
    private List<Contact> linkedContacts = new ArrayList<>();

    @Column(name = "relationship_label")
    private String relationshipLabel; // e.g. "Manager", "Friend", "Colleague"

    public Contact() {
    }

    public Contact(String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(Contact relatedTo) {
        this.relatedTo = relatedTo;
    }

    public List<Contact> getLinkedContacts() {
        return linkedContacts;
    }

    public void setLinkedContacts(List<Contact> linkedContacts) {
        this.linkedContacts = linkedContacts;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public void setRelationshipLabel(String relationshipLabel) {
        this.relationshipLabel = relationshipLabel;
    }
}

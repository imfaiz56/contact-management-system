package com.faiz.contactmanagement.repository;

import com.faiz.contactmanagement.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);

    Optional<Contact> findByPhoneNumber(String phoneNumber);

    List<Contact> findByFullNameContainingIgnoreCase(String name);

    List<Contact> findByPhoneNumberContaining(String phone);

    List<Contact> findByEmailContainingIgnoreCase(String email);

    List<Contact> findByRelatedToIsNull(); // top-level / root contacts, useful for building the tree
}

package org.example.mobilephonebookingservice.repository;

import org.example.mobilephonebookingservice.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {}

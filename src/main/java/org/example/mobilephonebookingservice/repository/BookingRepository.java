package org.example.mobilephonebookingservice.repository;

import org.example.mobilephonebookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}

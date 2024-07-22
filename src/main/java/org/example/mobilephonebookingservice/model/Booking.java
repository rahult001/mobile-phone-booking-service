package org.example.mobilephonebookingservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookedBy;
    private LocalDateTime bookedAt;

    @ManyToOne
    private Phone phone;
}


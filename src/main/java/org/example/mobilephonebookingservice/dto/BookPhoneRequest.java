package org.example.mobilephonebookingservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookPhoneRequest {

    @NotNull(message = "Phone ID is required")
    private Long phoneId;

    @NotEmpty(message = "Booked by is required")
    private String bookedBy;
}

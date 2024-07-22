package org.example.mobilephonebookingservice.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.mobilephonebookingservice.dto.BookPhoneRequest;
import org.example.mobilephonebookingservice.model.Phone;
import org.example.mobilephonebookingservice.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phones")
@Validated
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @ApiOperation(value = "Book a phone", response = Phone.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully booked the phone"),
            @ApiResponse(responseCode = "400", description = "Phone is already booked or other bad request"),
            @ApiResponse(responseCode = "404", description = "Phone not found")
    })
    @PostMapping("/book")
    public ResponseEntity<Phone> bookPhone(@Valid @RequestBody BookPhoneRequest request) {
        Phone phone = phoneService.bookPhone(request.getPhoneId(), request.getBookedBy());
        return ResponseEntity.ok(phone);
    }

    @ApiOperation(value = "Return a phone", response = Phone.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned the phone"),
            @ApiResponse(responseCode = "400", description = "Phone is not booked or other bad request"),
            @ApiResponse(responseCode = "404", description = "Phone not found")
    })
    @PostMapping("/return")
    public ResponseEntity<Phone> returnPhone(@ApiParam(value = "ID of the phone to return",
            required = true) @RequestParam Long phoneId) {
        Phone phone = phoneService.returnPhone(phoneId);
        return ResponseEntity.ok(phone);
    }

    @ApiOperation(value = "Get a Phone", response = Phone.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a Phone"),
            @ApiResponse(responseCode = "400", description = "Phone is not booked or other bad request"),
            @ApiResponse(responseCode = "404", description = "Phone not found")
    })
    @GetMapping("/get")
    public ResponseEntity<Phone> getPhone(@ApiParam(value = "ID of the phone to return",
            required = true) @RequestParam Long phoneId) {
        Phone phone = phoneService.getPhone(phoneId);
        return ResponseEntity.ok(phone);
    }
}

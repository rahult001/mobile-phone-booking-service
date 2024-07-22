package org.example.mobilephonebookingservice.service;

import org.example.mobilephonebookingservice.exception.PhoneNotAvailableException;
import org.example.mobilephonebookingservice.exception.PhoneNotFoundException;
import org.example.mobilephonebookingservice.model.Booking;
import org.example.mobilephonebookingservice.model.Phone;
import org.example.mobilephonebookingservice.repository.BookingRepository;
import org.example.mobilephonebookingservice.repository.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PhoneServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PhoneService phoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookPhoneSuccess() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(true);

        when(phoneRepository.findById(1L)).thenReturn(Optional.of(phone));
        when(phoneRepository.save(any(Phone.class))).thenReturn(phone);

        Phone bookedPhone = phoneService.bookPhone(1L, "John Doe");

        assertNotNull(bookedPhone);
        assertFalse(bookedPhone.isAvailable());
        verify(phoneRepository, times(1)).save(phone);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(notificationService, times(1)).sendNotification("Phone booked: Samsung Galaxy S9");
    }

    @Test
    void testBookPhoneNotFound() {
        when(phoneRepository.findById(1L)).thenReturn(Optional.empty());

        PhoneNotFoundException exception = assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.bookPhone(1L, "John Doe");
        });

        assertEquals("Phone not found", exception.getMessage());
    }

    @Test
    void testBookPhoneAlreadyBooked() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(false);

        when(phoneRepository.findById(1L)).thenReturn(Optional.of(phone));

        PhoneNotAvailableException exception = assertThrows(PhoneNotAvailableException.class, () -> {
            phoneService.bookPhone(1L, "John Doe");
        });

        assertEquals("Phone is already booked", exception.getMessage());
    }

    @Test
    void testReturnPhoneSuccess() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(false);

        when(phoneRepository.findById(1L)).thenReturn(Optional.of(phone));
        when(phoneRepository.save(any(Phone.class))).thenReturn(phone);

        Phone returnedPhone = phoneService.returnPhone(1L);

        assertNotNull(returnedPhone);
        assertTrue(returnedPhone.isAvailable());
        verify(phoneRepository, times(1)).save(phone);
        verify(notificationService, times(1)).sendNotification("Phone returned: Samsung Galaxy S9");
    }

    @Test
    void testReturnPhoneNotFound() {
        when(phoneRepository.findById(1L)).thenReturn(Optional.empty());

        PhoneNotFoundException exception = assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.returnPhone(1L);
        });

        assertEquals("Phone not found", exception.getMessage());
    }

    @Test
    void testReturnPhoneNotBooked() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(true);

        when(phoneRepository.findById(1L)).thenReturn(Optional.of(phone));

        PhoneNotAvailableException exception = assertThrows(PhoneNotAvailableException.class, () -> {
            phoneService.returnPhone(1L);
        });

        assertEquals("Phone is not booked", exception.getMessage());
    }

    @Test
    void testGetPhoneSuccess() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(true);

        when(phoneRepository.findById(anyLong())).thenReturn(Optional.of(phone));

        Phone retrievedPhone = phoneService.getPhone(1L);

        assertNotNull(retrievedPhone);
        assertEquals(1L, retrievedPhone.getId());
        assertEquals("Samsung Galaxy S9", retrievedPhone.getModel());
    }

    @Test
    void testGetPhoneNotFound() {
        when(phoneRepository.findById(anyLong())).thenReturn(Optional.empty());

        PhoneNotFoundException exception = assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.getPhone(1L);
        });

        assertEquals("Phone not found", exception.getMessage());
    }
}
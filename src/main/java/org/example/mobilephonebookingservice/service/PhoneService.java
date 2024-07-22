package org.example.mobilephonebookingservice.service;

import jakarta.transaction.Transactional;
import org.example.mobilephonebookingservice.exception.PhoneNotAvailableException;
import org.example.mobilephonebookingservice.exception.PhoneNotFoundException;
import org.example.mobilephonebookingservice.model.Booking;
import org.example.mobilephonebookingservice.model.Phone;
import org.example.mobilephonebookingservice.repository.BookingRepository;
import org.example.mobilephonebookingservice.repository.PhoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PhoneService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);

    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private NotificationService notificationService;

    /**
     * Books a phone for a user.
     * @param phoneId the ID of the phone to be booked
     * @param bookedBy the name of the person booking the phone
     * @return the updated Phone entity with availability set to false
     * @throws PhoneNotFoundException if the phone is not found
     * @throws PhoneNotAvailableException if the phone is already booked or being updated by another transaction
     */
    @Transactional
    public Phone bookPhone(Long phoneId, String bookedBy) {
        try {
            logger.info("Attempting to book phone with ID: {}", phoneId);
            // Retrieve the phone entity from the repository
            Phone phone = phoneRepository.findById(phoneId).orElseThrow(() -> new PhoneNotFoundException("Phone not found"));
            // Check if the phone is already booked
            if (!phone.isAvailable()) {
                logger.error("Phone with ID: {} is already booked", phoneId);
                throw new PhoneNotAvailableException("Phone is already booked");
            }
            // Mark the phone as booked (not available)
            phone.setAvailable(false);
            // Save the updated phone entity back to the repository
            phoneRepository.save(phone);

            // Create a new booking record
            Booking booking = new Booking();
            booking.setPhone(phone);
            booking.setBookedBy(bookedBy);
            booking.setBookedAt(LocalDateTime.now());
            // Save the booking record to the repository
            bookingRepository.save(booking);

            notificationService.sendNotification("Phone booked: " + phone.getModel());
            logger.info("Phone with ID: {} booked successfully by {}", phoneId, bookedBy);
            return phone;
        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking failure while booking phone with ID: {}", phoneId, e);
            // Handle case where the phone is being updated by another transaction
            throw new PhoneNotAvailableException("Phone is being updated by another transaction");
        }
    }

    /**
     * Returns a booked phone.
     * @param phoneId the ID of the phone to be returned
     * @return the updated Phone entity with availability set to true
     * @throws PhoneNotFoundException if the phone or booking is not found
     * @throws PhoneNotAvailableException if the phone is not booked or being updated by another transaction
     */
    @Transactional
    public Phone returnPhone(Long phoneId) {
        try {
            logger.info("Attempting to return phone with ID: {}", phoneId);
            // Retrieve the phone entity from the repository
            Phone phone = phoneRepository.findById(phoneId).orElseThrow(() -> new PhoneNotFoundException("Phone not found"));
            // Check if the phone is already available (not booked)
            if (phone.isAvailable()) {
                logger.error("Phone with ID: {} is not booked", phoneId);
                throw new PhoneNotAvailableException("Phone is not booked");
            }
            // Mark the phone as available
            phone.setAvailable(true);
            // Save the updated phone entity back to the repository
            phoneRepository.save(phone);

            notificationService.sendNotification("Phone returned: " + phone.getModel());
            logger.info("Phone with ID: {} returned successfully", phoneId);
            return phone;
        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking failure while returning phone with ID: {}", phoneId, e);
            // Handle case where the phone is being updated by another transaction
            throw new PhoneNotAvailableException("Phone is being updated by another transaction");
        }
    }

    /**
     * Retrieves a phone by its ID.
     * @param phoneId the ID of the phone to be retrieved
     * @return the Phone entity
     * @throws PhoneNotFoundException if the phone is not found
     */
    public Phone getPhone(Long phoneId) {
        return phoneRepository.findById(phoneId).orElseThrow(() -> new PhoneNotFoundException("Phone not found"));
    }
}

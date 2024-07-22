package org.example.mobilephonebookingservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mobilephonebookingservice.controller.PhoneController;
import org.example.mobilephonebookingservice.dto.BookPhoneRequest;
import org.example.mobilephonebookingservice.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private PhoneController phoneController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testHandlePhoneNotFoundException() throws Exception {
        when(phoneService.bookPhone(anyLong(), anyString())).thenThrow(new PhoneNotFoundException("Phone not found"));

        BookPhoneRequest request = new BookPhoneRequest();
        request.setPhoneId(1L);
        request.setBookedBy("John Doe");

        mockMvc.perform(post("/phones/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Phone not found"));
    }

    @Test
    void testHandlePhoneNotAvailableException() throws Exception {
        when(phoneService.returnPhone(anyLong())).thenThrow(new PhoneNotAvailableException("Phone is not booked"));

        mockMvc.perform(post("/phones/return")
                        .param("phoneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Phone is not booked"));
    }

    @Test
    void testHandleValidationExceptions() throws Exception {
        BookPhoneRequest request = new BookPhoneRequest();
        request.setPhoneId(null); // Invalid input
        request.setBookedBy(""); // Invalid input

        mockMvc.perform(post("/phones/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"phoneId\":\"Phone ID is required\",\"bookedBy\":\"Booked by is required\"}"));
    }

    @Test
    void testHandlePhoneNotFoundExceptionForGet() throws Exception {
        when(phoneService.getPhone(anyLong())).thenThrow(new PhoneNotFoundException("Phone not found"));

        mockMvc.perform(get("/phones/get")
                        .param("phoneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Phone not found"));
    }
}
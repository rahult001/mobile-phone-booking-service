package org.example.mobilephonebookingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mobilephonebookingservice.dto.BookPhoneRequest;
import org.example.mobilephonebookingservice.model.Phone;
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


class PhoneControllerTest {

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private PhoneController phoneController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testBookPhone() throws Exception {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(false);

        BookPhoneRequest request = new BookPhoneRequest();
        request.setPhoneId(1L);
        request.setBookedBy("John Doe");

        when(phoneService.bookPhone(anyLong(), anyString())).thenReturn(phone);

        mockMvc.perform(post("/phones/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"model\":\"Samsung Galaxy S9\",\"available\":false}"));
    }

    @Test
    void testReturnPhone() throws Exception {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(true);

        when(phoneService.returnPhone(anyLong())).thenReturn(phone);

        mockMvc.perform(post("/phones/return")
                        .param("phoneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"model\":\"Samsung Galaxy S9\",\"available\":true}"));
    }

    @Test
    void testGetPhone() throws Exception {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setModel("Samsung Galaxy S9");
        phone.setAvailable(true);

        when(phoneService.getPhone(anyLong())).thenReturn(phone);

        mockMvc.perform(get("/phones/get")
                        .param("phoneId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(phone)));
    }
}
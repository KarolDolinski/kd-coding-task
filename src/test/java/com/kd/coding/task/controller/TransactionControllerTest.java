package com.kd.coding.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.exception.DataNotFoundException;
import com.kd.coding.task.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;

import static com.kd.coding.task.util.TransactionFixtureFactory.CUSTOMER_ID;
import static com.kd.coding.task.util.TransactionFixtureFactory.TRANSACTION_ID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    public static final String TRANSACTION_PATH = "/transaction";

    public static final String TRANSACTION_ID_PATH = "/transaction/{transactionId}";

    public static final String CUSTOMER_ID_PARAM_NAME = "customerId";

    @MockBean
    TransactionService transactionService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveTransaction_validRequest_returnsOk() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .transactionAmount(100L)
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_PATH)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(transactionService).saveTransaction(request);
    }

    @Test
    void saveTransaction_invalidRequest_returnsBadRequest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_PATH)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(""))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void getTransaction_validRequest_returnsOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(TRANSACTION_ID_PATH, TRANSACTION_ID)
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService).getTransaction(TRANSACTION_ID);
    }

    @Test
    void getTransaction_throwsDataNotFound_returns404() throws Exception {
        doThrow(new DataNotFoundException(""))
                .when(transactionService).getTransaction(anyString());
        mvc.perform(MockMvcRequestBuilders
                        .get(TRANSACTION_ID_PATH, TRANSACTION_ID)
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(transactionService).getTransaction(TRANSACTION_ID);
    }


    @Test
    void removeTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete(TRANSACTION_ID_PATH, TRANSACTION_ID)
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService).removeTransaction(TRANSACTION_ID);
    }

    @Test
    void getTransactions_validRequest_returnsOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(TRANSACTION_PATH)
                        .param(CUSTOMER_ID_PARAM_NAME, CUSTOMER_ID)
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService).queryTransaction(CUSTOMER_ID);
    }


    @Test
    void getTransactions_invalidRequest_returnsOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(TRANSACTION_PATH)
                        .param(CUSTOMER_ID_PARAM_NAME, "")
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(transactionService);
    }
}
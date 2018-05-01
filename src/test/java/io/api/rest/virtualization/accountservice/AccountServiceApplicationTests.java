package io.api.rest.virtualization.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.api.rest.virtualization.accountservice.entity.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAccounts() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    public void getAccount() throws Exception {
        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.accountName", is("Account 1")))
                .andExpect(jsonPath("$.accountNo", is("0001")))
                .andExpect(jsonPath("$.accountType", is("Savings")))
                .andExpect(jsonPath("$.customerId", is("1")));
    }

    @Test
    public void getNonExistingAccount() throws Exception {
        mockMvc.perform(get("/accounts/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void createAccount() throws Exception {
        Account account = Account.builder().id("10").accountName("Account 10").accountNo("0010").accountType("Current").customerId("10").build();
        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated());
        // Verify created account
        mockMvc.perform(get("/accounts/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("10")))
                .andExpect(jsonPath("$.accountName", is("Account 10")))
                .andExpect(jsonPath("$.accountNo", is("0010")))
                .andExpect(jsonPath("$.accountType", is("Current")))
                .andExpect(jsonPath("$.customerId", is("10")));
    }

    @Test
    @Transactional
    public void updateAccount() throws Exception {
        // Create account
        Account account = Account.builder().id("10").accountName("Account 10").accountNo("0010").accountType("Current").customerId("10").build();
        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated());
        // Update account
        mockMvc.perform(put("/accounts/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"accountName\":\"Account 10 Updated\"}"))
                .andExpect(status().isOk());
        // Verify updated content
        mockMvc.perform(get("/accounts/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("10")))
                .andExpect(jsonPath("$.accountName", is("Account 10 Updated")))
                .andExpect(jsonPath("$.accountNo", is("0010")))
                .andExpect(jsonPath("$.accountType", is("Current")))
                .andExpect(jsonPath("$.customerId", is("10")));
    }

    @Test
    @Transactional
    public void updateNonExistingAccount() throws Exception {
        mockMvc.perform(put("/accounts/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"accountName\":\"Account 10 Updated\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteAccount() throws Exception {
        // Create account
        Account account = Account.builder().id("10").accountName("Account 10").accountNo("0010").accountType("Current").customerId("10").build();
        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated());
        // Delete account
        mockMvc.perform(delete("/accounts/10"))
                .andExpect(status().isNoContent());
        // Verify delete is successful
        mockMvc.perform(get("/accounts/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteNonExistingAccount() throws Exception {
        mockMvc.perform(delete("/accounts/10"))
                .andExpect(status().isNotFound());
    }
}

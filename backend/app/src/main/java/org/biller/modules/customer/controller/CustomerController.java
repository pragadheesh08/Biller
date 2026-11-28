package org.biller.modules.customer.controller;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

import org.biller.modules.customer.dto.CreateCustomerRequest;
import org.biller.modules.customer.dto.CustomerResponse;
import org.biller.modules.customer.dto.ListCustomerResponse;
import org.biller.modules.customer.model.Customer;
import org.biller.modules.customer.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository){
        this.repository = repository;
    }
    @GetMapping
    public ListCustomerResponse listCustomers(){
        try {
            return new ListCustomerResponse("Success",this.repository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ListCustomerResponse("Failed",null);
        }
    }
    @PostMapping
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request){
        Customer customer = new Customer(
            0l, 
            request.name(), 
            request.contact_no(), 
            request.address_line1(), 
            request.address_line2(), 
            request.address_line3(), 
            request.GSTIN(),
            Instant.now()
        );
        try {
            customer = this.repository.create(customer);
            return new CustomerResponse("Success",customer);
        } catch(SQLException e){
            e.printStackTrace();
            return new CustomerResponse("Failed",null);
        }
        
    }
    @GetMapping ("/customers/{id}")
    public CustomerResponse customerdetails(Long id){
        try{
            Optional<Customer> a = this.repository.findById(id);
            if (a.isEmpty()){
                return new CustomerResponse("Not Found", null);
            }
            return new CustomerResponse("Success",a.get());  
        } catch (SQLException e) {
            e.printStackTrace();
            return new CustomerResponse("Failed",null);
        }
    }
}

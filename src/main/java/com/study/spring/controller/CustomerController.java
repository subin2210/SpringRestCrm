package com.study.spring.controller;

import com.study.spring.entity.Customer;
import com.study.spring.exception.CustomerException;
import com.study.spring.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable int customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if(customer == null)
            throw  new CustomerException("Customer not found for customer id = " + customerId);
        return customer;
    }

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer){
        customer.setId(0);
        customerService.saveCustomer(customer);
        return customer;
    }

    @PutMapping("/customer")
    public Customer updateCustomer(@RequestBody Customer customer){
        customerService.saveCustomer(customer);
        return customer;
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if(customer == null)
            throw  new CustomerException("Customer not found for customer id = " + customerId);
        customerService.deleteCustomer(customer.getId());

        return "customer deleted for customer id = "+ customerId;
    }


}

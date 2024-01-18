package com.imse.ticketshop.service.impl;

import com.imse.ticketshop.entity.Customer;
import com.imse.ticketshop.repository.CustomerRepository;
import com.imse.ticketshop.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }


    @Override
    public List<Customer> getAllCustomer() {
        return customerRepo.findAll();
    }

    @Override
    public Boolean doesCustomerWithIdExist(UUID id) {
        return customerRepo.existsById(id);
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customerRepo.findById(id).orElse(null);
    }
}

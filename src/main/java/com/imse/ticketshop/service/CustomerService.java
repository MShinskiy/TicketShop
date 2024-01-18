package com.imse.ticketshop.service;

import com.imse.ticketshop.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CustomerService {

    List<Customer> getAllCustomer();
    Boolean doesCustomerWithIdExist(UUID id);
    Customer getCustomerById(UUID id);
}

package com.imse.ticketshop.service;

import com.imse.ticketshop.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    List<Customer> getAllCustomer();
}

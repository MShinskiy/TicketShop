package com.imse.ticketshop.service;

import com.imse.ticketshop.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    Customer getCustomer();
}

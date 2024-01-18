package com.imse.ticketshop.service;

import com.imse.ticketshop.entity.Customer;
import com.imse.ticketshop.entity.dto.DemographicsReport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CustomerService {

    List<DemographicsReport> getDataForReport();
    List<Customer> getAllCustomers();
    Boolean doesCustomerWithIdExist(UUID id);
    Customer getCustomerById(UUID id);
}

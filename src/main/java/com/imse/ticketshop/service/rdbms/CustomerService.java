package com.imse.ticketshop.service.rdbms;

import com.imse.ticketshop.entity.rdbms.Customer;
import com.imse.ticketshop.entity.rdbms.dto.DemographicsReport;
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

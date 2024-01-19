package com.imse.ticketshop.service.rdbms.impl;

import com.imse.ticketshop.entity.rdbms.Customer;
import com.imse.ticketshop.entity.rdbms.dto.DemographicsReport;
import com.imse.ticketshop.repository.rdbms.CustomerRepository;
import com.imse.ticketshop.service.rdbms.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }


    @Override
    public List<Customer> getAllCustomers() {
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

    @Override
    public List<DemographicsReport> getDataForReport() {

        var x = customerRepo.demographicsReportData().stream()
                .map(obj -> new DemographicsReport(
                        String.valueOf(obj[0]),
                        String.valueOf(obj[1]),
                        String.valueOf(obj[2]),
                        Integer.parseInt(String.valueOf(obj[3]))
                )).collect(Collectors.toList());
        return x;
    }
}

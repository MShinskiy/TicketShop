package com.imse.ticketshop.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface OrderService {
    void addNewOrder(List<Map<String, String>> tableData);
}

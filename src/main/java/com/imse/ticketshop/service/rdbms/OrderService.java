package com.imse.ticketshop.service.rdbms;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public interface OrderService {
    void addNewOrder(List<Map<String, String>> tableData, UUID uuid);
}

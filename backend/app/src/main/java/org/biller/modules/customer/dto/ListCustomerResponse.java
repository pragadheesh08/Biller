package org.biller.modules.customer.dto;

import java.util.List;


import org.biller.modules.customer.model.Customer;

public record ListCustomerResponse(
    String status,
    List<Customer> customers
) {
    public ListCustomerResponse {
        if (customers == null) {
            customers = List.of();
        }
    }
}
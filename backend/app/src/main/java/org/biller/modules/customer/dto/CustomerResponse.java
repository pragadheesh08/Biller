package org.biller.modules.customer.dto;

import org.biller.modules.customer.model.Customer;

public record CustomerResponse (
    String status,
    Customer details
) {}

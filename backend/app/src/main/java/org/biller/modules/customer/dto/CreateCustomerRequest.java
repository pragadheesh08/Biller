package org.biller.modules.customer.dto;

public record CreateCustomerRequest(
    String name,
    String contact_no,
    String address_line1,
    String address_line2,
    String address_line3,
    String GSTIN
) {}

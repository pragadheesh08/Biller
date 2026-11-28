package org.biller.modules.customer.model;

import java.time.Instant;

public record Customer (
    Long id,
    String name,
    String contact_no,
    String address_line1,
    String address_line2,
    String address_line3,
    String GSTIN,
    Instant created_at
){}
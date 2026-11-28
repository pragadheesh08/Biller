package org.biller.modules.customer.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.biller.modules.customer.model.Customer;

public interface CustomerRepository {
    
    Customer create(Customer c)
        throws SQLException;

    List<Customer> findAll()
        throws SQLException;

    Optional<Customer> findById(long id)
        throws SQLException;

    Optional<Customer> findByCustomername(String name)
        throws SQLException;
    
    boolean updateCustomername(long id, String newName)
        throws SQLException;

    Optional<Customer> deleteCustomer(long ID)
        throws SQLException;

}

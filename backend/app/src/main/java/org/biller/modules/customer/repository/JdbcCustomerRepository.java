
package org.biller.modules.customer.repository;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.biller.modules.customer.model.Customer;
import org.springframework.stereotype.Repository;
@Repository 
public class JdbcCustomerRepository implements CustomerRepository {

    private final DataSource dataSource;

    public JdbcCustomerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 1. Create Customer
    @Override
    public Customer create(Customer c) throws SQLException {

        String sql = """
            INSERT INTO customers
            (name, contact_no, address_line1, address_line2, address_line3, GSTIN)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id, created_at
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.name());
            ps.setString(2, String.valueOf(c.contact_no()));
            ps.setString(3, c.address_line1());
            ps.setString(4, c.address_line2());
            ps.setString(5, c.address_line3());
            ps.setString(6, c.GSTIN());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getLong("id"),
                        c.name(),
                        c.contact_no(),
                        c.address_line1(),
                        c.address_line2(),
                        c.address_line3(),
                        c.GSTIN(),
                        rs.getTimestamp("created_at").toInstant()
                    );
                }
            }
        }

        return null;
    }

    // 2. Find All Customers
    @Override
    public List<Customer> findAll() throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customers";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }
        }

        return customers;
    }

    // 3. Find Customer By ID
    @Override
    public Optional<Customer> findById(long id)
            throws SQLException {

        String sql = "SELECT * FROM customers WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCustomer(rs));
                }
            }
        }

        return Optional.empty();
    }

    // 4. Find Customer By Name
    @Override
    public Optional<Customer> findByCustomername(String name)
            throws SQLException {

        String sql = "SELECT * FROM customers WHERE name = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCustomer(rs));
                }
            }
        }

        return Optional.empty();
    }

    // 5. Update Customer Name
    @Override
    public boolean updateCustomername(long id, String newName)
            throws SQLException {

        String sql = """
            UPDATE customers
            SET name = ?
            WHERE id = ?
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setLong(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    // 6. Delete Customer
    @Override
    public Optional<Customer> deleteCustomer(long ID)
            throws SQLException {

        String sql = """
            DELETE FROM customers
            WHERE id = ?
            RETURNING *
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, ID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCustomer(rs));
                }
            }
        }

        return Optional.empty();
    }

    // Helper method
    private Customer mapCustomer(ResultSet rs) throws SQLException {

        Timestamp timestamp = rs.getTimestamp("created_at");

        return new Customer(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("contact_no"),
            rs.getString("address_line1"),
            rs.getString("address_line2"),
            rs.getString("address_line3"),
            rs.getString("GSTIN"),
            timestamp != null ? timestamp.toInstant() : null
        );
    }
}

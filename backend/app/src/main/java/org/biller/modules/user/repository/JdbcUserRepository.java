package org.biller.modules.user.repository;

import org.biller.modules.user.model.User;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.*;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final DataSource dataSource;

    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final PasswordEncoder encoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public String hash(String password) {
        return encoder.encode(password);
    }

    public boolean verify(String password, String hash) {
        return encoder.matches(password, hash);
    }

    private User mapUser(ResultSet rs) throws SQLException {

        Timestamp timestamp = rs.getTimestamp("created_at");

        return new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password_hash"),
            timestamp.toInstant()
        );
    }

    @Override
    public User create(String username, String email, String password)
        throws SQLException {

        String sql = """
            INSERT INTO users (username, email, password_hash)
            VALUES (?, ?, ?)
            RETURNING id, username, email, created_at, password_hash
            """;

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, this.hash(password));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }

        throw new SQLException("Failed to create user");
    }

    @Override
    public Optional<User> findById(long id)
            throws SQLException {

        String sql = """
            SELECT id, username, email, created_at
            FROM users
            WHERE id = ?
            """;

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username)
            throws SQLException {

        String sql = """
            SELECT id, username, email, created_at, password_hash
            FROM users
            WHERE username = ?
            """;

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean updateUsername(long id, String newName) throws SQLException {

        String sql = """
            UPDATE users
            SET username = ?
            WHERE id = ?
            """;

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(1, newName);
            statement.setLong(2, id);

            return statement.executeUpdate() > 0;
        }
    }

    private Optional<User> getUserByName(String username) throws SQLException {
        String sql = """
            SELECT id, username, email, password_hash, created_at
            FROM users
            WHERE username = ?
            """;

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
                else{
                    //User not found
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean verifyPassword(String username, String password) throws SQLException {
        Optional<User> user = getUserByName(username);
        if(user.isEmpty())
        {
            throw new SQLException("User not found");
        };

        User userObj = user.get();


        return this.verify(password, userObj.passwordHash());
    }
}
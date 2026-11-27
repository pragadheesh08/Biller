package org.biller.modules.user.repository;

import java.sql.SQLException;
import java.util.Optional;

import org.biller.modules.user.model.User;

public interface UserRepository {

    User create(String username, String email, String password)
        throws SQLException;

    Optional<User> findById(long id)
        throws SQLException;

    Optional<User> findByUsername(String username)
        throws SQLException;

    boolean updateUsername(long id, String newName)
        throws SQLException;

    boolean verifyPassword(String username, String password)
        throws SQLException;

}
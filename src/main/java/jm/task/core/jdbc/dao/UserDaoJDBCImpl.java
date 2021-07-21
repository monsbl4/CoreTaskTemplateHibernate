package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//
public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }
    public void createUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS mydb.users2 (id BIGINT not null auto_increment," +
                    "name VARCHAR(40)," +
                    " last_name VARCHAR(100)," +
                    " age tinyint, " +
                    "PRIMARY KEY (id))");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users2");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser( String name,  String lastName,  byte age) {
        Connection connection = Util.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users2(name, last_name, age) VALUES (?,?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            try {
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM users2 where id=?")) {
            statement.setLong(1,id);
            statement.executeUpdate();
            try {
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        Connection connection = Util.getConnection();
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * from users2");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
            try {
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("DELETE FROM users2 WHERE id>0");
            try {
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

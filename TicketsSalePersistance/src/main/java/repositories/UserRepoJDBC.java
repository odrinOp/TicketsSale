package repositories;

import interfaces.IUserRepo;
import model.Entity;
import model.User;
import util.Utils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class UserRepoJDBC implements IUserRepo {
    JdbcUtils utils;

    public UserRepoJDBC(Properties properties) {
        utils = new JdbcUtils(properties);
    }

    @Override
    public User findBy(User user) {
        Connection connection = utils.getConnection();
        try(Statement statement = connection.createStatement()){
            try(PreparedStatement prStm = connection.prepareStatement("select * from Users where username=? and password=?")){
                prStm.setString(1,user.getUsername());
                prStm.setString(2,user.getPassword());
                ResultSet resultSet = prStm.executeQuery();
                boolean res = resultSet.next();
                if(res)
                    return new User(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("name"));
                return null;

            }
        } catch (SQLException e) {
            Utils.logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public User findBy(String username) {
        Connection connection = utils.getConnection();
        try(Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Users WHERE username=" + username +";")){
                resultSet.next();
                return new User(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("name"));

            }
        } catch (SQLException e) {
            Utils.logger.error(e.getMessage());
            return null;
        }
    }


    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public User remove(User user) {
        return null;
    }

    @Override
    public User findOne(Integer integer) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public List<User> findAll() {
        Connection connection = utils.getConnection();
        List<User> users = new LinkedList<>();
        try(Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Users;")){
                while(resultSet.next())
                users.add(new User(resultSet.getString("username")));

            }
        } catch (SQLException e) {
            Utils.logger.error(e.getMessage());
            return users;
        }
        return users;
    }
}

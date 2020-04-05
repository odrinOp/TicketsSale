package repositories;

import interfaces.ISaleRepo;
import model.Sale;
import util.Utils;
import validators.SaleValidator;
import validators.ValidatorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class SaleRepoJDBC implements ISaleRepo {

    JdbcUtils utils;
    SaleValidator validator;

    public SaleRepoJDBC(Properties properties,SaleValidator validator) {
        this.utils = new JdbcUtils(properties);
        this.validator = validator;
    }

    @Override
    public Sale add(Sale sale) throws ValidatorException {
        validator.validate(sale);
        Connection connection = utils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO Sales(name,no_of_seats,game_id) VALUES (?,?,?)")) {
            statement.setString(1,sale.getName());
            statement.setInt(2,sale.getNo_of_seats());
            statement.setInt(3,sale.getGame_id());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            Utils.logger.error(e.getMessage());
            return sale;
        }
    }

    @Override
    public Sale remove(Sale sale) {
        return null;
    }

    @Override
    public Sale findOne(Integer integer) {
        return null;
    }

    @Override
    public Sale update(Sale sale) {
        return null;
    }

    @Override
    public List<Sale> findAll() {
        return null;
    }
}

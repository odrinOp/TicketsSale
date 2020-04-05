package repositories;

import interfaces.IGameRepo;
import model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import util.Utils;
import validators.GameValidator;
import validators.ValidatorException;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class GameRepoJDBC implements IGameRepo {

    JdbcUtils utils;
    GameValidator validator;
    private Logger logger = (Logger) LogManager.getLogger();
    public GameRepoJDBC(Properties properties,GameValidator validator) {

        utils = new JdbcUtils(properties);
        this.validator = validator;
    }


    @Override
    public Game add(Game game) throws ValidatorException {
        return null;
    }

    @Override
    public Game remove(Game game) {
        return null;
    }

    @Override
    public Game findOne(Integer integer) {
        Connection connection = utils.getConnection();
        try(Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE game_id="+integer)){
                resultSet.next();
                return new Game(resultSet.getInt("game_id"),
                        resultSet.getString("home_team"),
                        resultSet.getString("away_team"),
                        resultSet.getString("game_date"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("available_seats"));

            }
        } catch (SQLException e) {
            Utils.logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Game update(Game game) throws ValidatorException{

        validator.validate(game);
        logger.traceExit("The game has been validated!");
        Connection connection = utils.getConnection();
        logger.traceExit("The connection to database is complete!");
        try(Statement statement = connection.createStatement()){

            String update = "UPDATE Games SET available_seats="+game.getAvailableSeats()+" WHERE game_id="+game.getId();
            statement.executeUpdate(update);
            logger.traceExit("The game has been updated");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return game;
        }
        return null;
    }

    @Override
    public List<Game> findAll() {

        List<Game> list = new LinkedList<>();
        Connection connection = utils.getConnection();
        logger.traceExit("Connection to database completed!");
        try(Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Games")){
                while(resultSet.next()){
                    int id = resultSet.getInt("game_id");
                    String home_team = resultSet.getString("home_team");
                    String away_team = resultSet.getString("away_team");
                    String data = resultSet.getString("game_date");
                    double pret = resultSet.getDouble("price");
                    int nrLocuri = resultSet.getInt("available_seats");
                    list.add(new Game(id,home_team,away_team,data,pret,nrLocuri));
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Getting all the games!");
        return list;
    }

    @Override
    public List<Game> findAllSorted() {
        List<Game> list = new LinkedList<>();
        Connection connection = utils.getConnection();
        logger.traceExit("Connection to database completed!");
        try(Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Games ORDER BY available_seats DESC")){
                while(resultSet.next()){
                    int id = resultSet.getInt("game_id");
                    String home_team = resultSet.getString("home_team");
                    String away_team = resultSet.getString("away_team");
                    String data = resultSet.getString("game_date");
                    double pret = resultSet.getDouble("price");
                    int nrLocuri = resultSet.getInt("available_seats");
                    list.add(new Game(id,home_team,away_team,data,pret,nrLocuri));
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Sorted list of all games");
        return list;
    }
}

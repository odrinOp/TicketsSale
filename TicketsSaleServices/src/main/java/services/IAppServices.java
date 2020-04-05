package services;

import model.Game;
import model.Sale;
import model.User;

public interface IAppServices {

    void login(User user,IAppObserver client) throws AppException;
    void logout(User user,IAppObserver client) throws AppException;
    void makeSale(Sale sale) throws AppException;
    Game[] getListOfGames() throws AppException;

}

package services;

import model.Game;

public interface IAppObserver {
    void updateGameList(Game[] games) throws AppException;
}

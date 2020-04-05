package interfaces;

import model.Game;

import java.util.List;

public interface IGameRepo extends ICrudRepo<Integer, Game> {

    List<Game> findAllSorted();
}

package objectprotocol;

import model.Game;

public class UpdateGamesResponse implements UpdateResponse {
    private Game[] games;

    public UpdateGamesResponse(Game[] games) {
        this.games = games;
    }

    public Game[] getGames() {
        return games;
    }
}

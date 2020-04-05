package objectprotocol;

import model.Game;

public class GetGamesResponse implements Response {
    private Game[] games;

    public GetGamesResponse(Game[] games) {
        this.games = games;
    }

    public Game[] getGames() {
        return games;
    }
}

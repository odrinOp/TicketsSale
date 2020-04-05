package validators;

import model.Game;

public class GameValidator implements Validator<Game> {
    @Override
    public void validate(Game game) throws ValidatorException {
        if(game == null)
            throw new ValidatorException("The object is null!");
        if (game.getAvailableSeats() < 0)
            throw new ValidatorException("The number of seats must be higher or equal to 0!");
    }
}

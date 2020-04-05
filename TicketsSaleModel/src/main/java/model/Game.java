package model;

public class Game extends Entity<Integer> {
    String home_team;
    String away_team;
    String game_date;
    int available_seats;
    double price;


    public Game(int gameID,String home_team, String away_team, String game_date,double price, int available_seats) {
        setId(gameID);
        this.home_team = home_team;
        this.away_team = away_team;
        this.game_date = game_date;
        this.price = price;
        this.available_seats = available_seats;
    }

    public String getHomeTeam() {
        return home_team;
    }

    public void setHomeTeam(String home_team) {
        this.home_team = home_team;
    }

    public String getAwayTeam() {
        return away_team;
    }

    public void setAwayTeam(String away_team) {
        this.away_team = away_team;
    }

    public String getGameDate() {
        return game_date;
    }

    public void setGameDate(String game_date) {
        this.game_date = game_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return available_seats;
    }

    public String getTicketsAvailable(){
        if (available_seats == 0)
            return "SOLD OUT";
        return String.valueOf(available_seats);
    }

    public void setAvailableSeats(int available_seats) {
        this.available_seats = available_seats;
    }

    @Override
    public String toString() {

        String str = getId() + "     " + getHomeTeam() + " vs " + getAwayTeam() + "     Date: " + getGameDate() + "     Seats: ";
        if (getAvailableSeats() == 0)
            str += "SOLD OUT";
        else
            str += getAvailableSeats();

        return str;
    }
}

package model;

public class Sale extends Entity<Integer> {
    int game_id;
    private String name;
    private int no_of_seats;

    public Sale(int game_id,String name, int no_of_seats) {
        setId(0);
        this.game_id = game_id;
        this.name = name;
        this.no_of_seats = no_of_seats;
    }

    public Sale(int id,int game_id,String name, int no_of_seats) {
        setId(id);
        this.name = name;
        this.game_id = game_id;
        this.no_of_seats = no_of_seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo_of_seats() {
        return no_of_seats;
    }

    public void setNo_of_seats(int no_of_seats) {
        this.no_of_seats = no_of_seats;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }
}

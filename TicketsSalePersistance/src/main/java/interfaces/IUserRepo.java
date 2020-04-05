package interfaces;

import model.User;

import java.util.List;

public interface IUserRepo extends ICrudRepo<Integer, User> {

    public User findBy(User user);
    public User findBy(String username);

}

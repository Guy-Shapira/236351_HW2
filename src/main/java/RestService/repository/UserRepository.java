package RestService.repository;

import TaxiRide.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private ArrayList<User> users;
    private long idIndex = 1;

    public UserRepository(){
        users = new ArrayList<>();
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User newUser){
        if (users.contains(newUser)){
            // TODO: throw better exception
            throw new RuntimeException("already exists");
        }
        newUser.setId(idIndex ++);
        users.add(newUser);
        return newUser;
    }

    public void delete(User user){
        users.remove(user);
    }

}

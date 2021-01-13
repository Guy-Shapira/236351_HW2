package RestService.repository;

import TaxiRide.User;

import java.util.ArrayList;
import java.util.List;
import Utils.UserRepoInstance;


public class UserRepository {

    private ArrayList<UserRepoInstance> users;
    private long idIndex = 1;

    public UserRepository() {
        users = new ArrayList<>();
    }

    public List<UserRepoInstance> findAll() {
        return users;
    }

    public UserRepoInstance save(User newUser) {
        if (users.contains(newUser)) {
            // TODO: throw better exception
            throw new RuntimeException("already exists");
        }
        UserRepoInstance newUserRepo;
        if (!(newUser instanceof UserRepoInstance)) {
            newUserRepo = new UserRepoInstance(newUser);
        } else {
            newUserRepo = (UserRepoInstance) newUser;
        }

        newUserRepo.setId(idIndex++);

        users.add(newUserRepo);
        return newUserRepo;
    }

    public void delete(User user) {
        users.remove(user);
    }

    public long getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(long idIndex) {
        this.idIndex = idIndex;
    }

    public ArrayList<UserRepoInstance> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserRepoInstance> users) {
        this.users = users;
    }

    public UserRepoInstance changeStatusById(long Id) {
        for (UserRepoInstance user : this.users) {
            if (user.getId() == Id) {
                user.setStatus(UserRepoInstance.UserStatus.COMPLETE);
                return user;
            }
        }
        return null;
    }
}

package it.polimi.ingsw.server;

/*
    A thread-safe User List class that forbids multiple users with same username
    Can call addWithLimit to pass a capacity limit to respect during the add operation, if not respected return false
 */

public class UniqueUserList extends SafeList<User> {
    @Override
    public synchronized boolean add(User user) {
        for (User u: this) {
            if (u.getName().equals(user.getName())) {
                return false;
            }
        }
        return super.add(user);
    }

    public synchronized boolean addWithLimit(User user, int capacity) {
        if (size() < capacity) {
            return add(user);
        }
        return false;
    }
}

package it.polimi.ingsw.server;

/**
 * A thread-safe User List class that forbids multiple users with same username
 * Can call addWithLimit to pass a capacity limit to respect during the add operation, if not respected return false
 */
public class UniqueUserList extends SafeList<User> {

    /**
     * add a user to the list if there is no other user with the same name already
     * @param user the user to add to the list
     * @return if the user is added successfully
     */
    @Override
    public synchronized boolean add(User user) {
        for (User u: this) {
            if (u.getName().equals(user.getName())) {
                return false;
            }
        }
        return super.add(user);
    }

    /**
     * add a user to the list respecting user unicity and capacity limit
     * @param user the user to add to the list
     * @param capacity the maximum number of element accepted after the add method execution
     * @return if the user is added successfully
     */
    public synchronized boolean addWithLimit(User user, int capacity) {
        if (size() < capacity) {
            return add(user);
        }
        return false;
    }
}

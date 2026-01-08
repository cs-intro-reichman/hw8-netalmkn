public class Network {

    // Fields
    private User[] users;  // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network  with some users. The only purpose of this constructor is
     *  to allow testing the toString and getUser methods, before implementing other methods. */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }

    /** Finds in this network, and returns, the user that has the given name.
     *  If there is no such user, returns null.
     *  Notice that the method receives a String, and returns a User object. */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equals(name)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a new user with the given name to this network.
     *  If ths network is full, does nothing and returns false;
     *  If the given name is already a user in this network, does nothing and returns false;
     *  Otherwise, creates a new user with the given name, adds the user to this network, and returns true. */
    public boolean addUser(String name) {
        if (userCount == users.length) {
            return false; // network full
        }
        if (getUser(name) != null) {
            return false; // already exists
        }
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    /** Makes the user with name1 follow the user with name2. If successful, returns true.
     *  If any of the two names is not a user in this network,
     *  or if the "follows" addition failed for some reason, returns false. */
    public boolean addFollowee(String name1, String name2) {
        User u1 = getUser(name1);
        User u2 = getUser(name2);
        if (u1 == null || u2 == null) {
            return false;
        }
        if (name1.equals(name2)) { // optional: prevent self-follow
            return false;
        }
        return u1.addFollowee(name2);
    }

    /** For the user with the given name, recommends another user to follow. The recommended user is
     *  the user that has the maximal mutual number of followees as the user with the given name. */
    public String recommendWhoToFollow(String name) {
        User me = getUser(name);
        if (me == null) {
            return null;
        }

        User best = null;
        int bestMutual = -1;

        for (int i = 0; i < userCount; i++) {
            User candidate = users[i];

            // skip myself
            if (candidate.getName().equals(name)) {
                continue;
            }

            // skip users I already follow (recommended "to follow")
            if (me.follows(candidate.getName())) {
                continue;
            }

            int mutual = countMutualFollowees(me, candidate);
            if (mutual > bestMutual) {
                bestMutual = mutual;
                best = candidate;
            }
        }

        return (best == null) ? null : best.getName();
    }

    // Counts how many followees are common to u1 and u2
    private int countMutualFollowees(User u1, User u2) {
        int mutual = 0;
        String[] f1 = u1.getFollows();
        int c1 = u1.getCount();

        for (int i = 0; i < c1; i++) {
            if (u2.follows(f1[i])) {
                mutual++;
            }
        }
        return mutual;
    }

    /** Computes and returns the name of the most popular user in this network:
     *  The user who appears the most in the follow lists of all the users. */
    public String mostPopularUser() {
        if (userCount == 0) {
            return null;
        }

        String bestName = null;
        int bestCount = -1;

        for (int i = 0; i < userCount; i++) {
            String candidateName = users[i].getName();
            int count = followeeCount(candidateName);
            if (count > bestCount) {
                bestCount = count;
                bestName = candidateName;
            }
        }
        return bestName;
    }

    /** Returns the number of times that the given name appears in the follows lists of all
     *  the users in this network. Note: A name can appear 0 or 1 times in each list. */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            String[] follows = users[i].getFollows();
            int fCount = users[i].getCount();
            for (int j = 0; j < fCount; j++) {
                if (follows[j].equals(name)) {
                    count++;
                    break; // at most once per user's list
                }
            }
        }
        return count;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    public String toString() {
        String ans = "Network:\n";
        for (int i = 0; i < userCount; i++) {
            ans += users[i].toString() + "\n";
        }
        return ans;
    }
}

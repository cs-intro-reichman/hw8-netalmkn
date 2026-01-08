public class Network {

    private User[] users;
    private int userCount;

    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;

        users[0].addFollowee("Bar");
        users[2].addFollowee("Foo");
    }

    public int getUserCount() {
        return userCount;
    }

    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equals(name)) {
                return users[i];
            }
        }
        return null;
    }

    public boolean addUser(String name) {
        if (userCount == users.length) {
            return false;
        }
        if (getUser(name) != null) {
            return false;
        }
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    public boolean addFollowee(String name1, String name2) {
        User u1 = getUser(name1);
        User u2 = getUser(name2);
        if (u1 == null || u2 == null) {
            return false;
        }
        if (name1.equals(name2)) {
            return false;
        }
        return u1.addFollowee(name2);
    }

    public String recommendWhoToFollow(String name) {
        User me = getUser(name);
        if (me == null) {
            return null;
        }

        User best = null;
        int bestMutual = -1;

        for (int i = 0; i < userCount; i++) {
            User candidate = users[i];

            if (candidate.getName().equals(name)) {
                continue;
            }
            if (me.follows(candidate.getName())) {
                continue;
            }

            int mutual = me.countMutual(candidate);
            if (mutual > bestMutual) {
                bestMutual = mutual;
                best = candidate;
            }
        }

        return best == null ? null : best.getName();
    }

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

    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    public String toString() {
        String s = "Network:";
        if (userCount > 0) {
            s += "\n";
        }
        for (int i = 0; i < userCount; i++) {
            s += users[i].toString();
            if (i < userCount - 1) {
                s += "\n";
            }
        }
        return s;
    }
}

public class User {

    public static final int maxfCount = 10;

    private String name;
    private String[] follows;
    private int fCount;

    public User(String name) {
        this.name = name;
        this.follows = new String[maxfCount];
        this.fCount = 0;
    }

    public User(String name, boolean gettingStarted) {
        this(name);
        if (gettingStarted) {
            follows[0] = "Foo";
            follows[1] = "Bar";
            follows[2] = "Baz";
            normalizeCount();
        }
    }

    private void normalizeCount() {
        int c = 0;
        while (c < follows.length && follows[c] != null) {
            c++;
        }
        fCount = c;
    }

    public String getName() {
        return name;
    }

    public String[] getFollows() {
        return follows;
    }

    public int getCount() {
        normalizeCount();
        return fCount;
    }
    /**
     * Returns the number of users this user follows (for testing purposes).
     */
    public int getfCount() {
        return getCount();
    }

    /**
     * Returns the follows array (for testing purposes).
     */
    public String[] getfFollows() {
        return getFollows();
    }

    public String toString() {
        normalizeCount();
        String ans = name + " -> ";
        for (int i = 0; i < fCount; i++) {
            ans += follows[i] + " ";
        }
        return ans;
    }

    public boolean follows(String name) {
        for (int i = 0; i < follows.length; i++) {
            if (follows[i] != null && follows[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean addFollowee(String name) {
        normalizeCount();
        if (follows(name)) { // בדיקה האם כבר עוקב אחריו
            return false;
        }
        if (fCount == follows.length) {
            return false;
        }
        follows[fCount] = name;
        fCount++;
        return true;
    }

    public boolean removeFollowee(String name) {
        normalizeCount();
        int idx = -1;
        for (int i = 0; i < fCount; i++) {
            if (follows[i] != null && follows[i].equals(name)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            return false;
        }

        for (int i = idx; i < fCount - 1; i++) {
            follows[i] = follows[i + 1];
        }
        follows[fCount - 1] = null;
        fCount--;
        return true;
    }

    public int countMutual(User other) {
        normalizeCount();
        int mutual = 0;

        for (int i = 0; i < fCount; i++) {
            String x = follows[i];
            if (x == null) {
                continue;
            }
            if (!other.follows(x)) {
                continue;
            }
            boolean counted = false;
            for (int k = 0; k < i; k++) {
                if (follows[k] != null && follows[k].equals(x)) {
                    counted = true;
                    break;
                }
            }
            if (!counted) {
                mutual++;
            }
        }
        return mutual;
    }
    public boolean isFriendOf(User other) {
        return this.follows(other.getName()) && other.follows(this.getName());
    }
}

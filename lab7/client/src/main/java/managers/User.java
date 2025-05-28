package managers;

public class User {
    private final String name;
    private final char[] passwd;
    private static User instance;
    private static boolean isAuth = false;

    private User(String name, char[] passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public static User getInstance(String name, char[] passwd) {
        if (instance == null) {
            instance = new User(name, passwd);
        }
        return instance;
    }

    public static User getInstance() {
        if (instance == null) {
            throw new IllegalStateException("User is not initialized. Please login first");
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public String getName() {
        return name;
    }

    public char[] getPasswd() {
        return passwd;
    }

    public static boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}

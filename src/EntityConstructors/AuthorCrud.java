package EntityConstructors;

public class AuthorCrud {
    private int authId;
    private String authName;

    public AuthorCrud() {
        // Default constructor
    }

    public AuthorCrud(int authId, String authName) {
        this.authId = authId;
        this.authName = authName;
    }

    // Getters and setters

    public AuthorCrud(String authName) {
        this.authName = authName;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authId=" + authId +
                ", authName='" + authName + '\'' +
                '}';
    }
}

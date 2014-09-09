package scopes;

public class SessionScopedService {

    private final String name;

    public SessionScopedService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

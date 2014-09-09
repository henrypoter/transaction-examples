package scopes;

public class RequestScopedService {

    private final String name;

    public RequestScopedService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

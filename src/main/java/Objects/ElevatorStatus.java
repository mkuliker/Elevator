package Objects;

public enum ElevatorStatus {
    UP("^"),
    DOWN("v"),
    OFF("o");

    private final String title;

    ElevatorStatus(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

package Objects;

public enum FloorButtonStatus {
    UP("^"),
    DOWN("v"),
    OFF("O");

    private final String title;

    FloorButtonStatus(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

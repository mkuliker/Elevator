package Objects;

public class GroupImpl implements Group{
    UObject object;

    public GroupImpl(UObject object) {
        this.object = object;
    }

    public GroupImpl(int count, int targetFloor) {
        this.object = new CustomObject();
        object.setProperty("count", count);
        object.setProperty("targetFloor", targetFloor);
    }

    @Override
    public int getCount() {
        return (int) object.getProperty("count");
    }

    @Override
    public int getTargetFloor() {
        return (int) object.getProperty("targetFloor");
    }
}

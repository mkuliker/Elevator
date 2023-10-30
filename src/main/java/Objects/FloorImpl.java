package Objects;

import java.util.List;

public class FloorImpl implements Floor{

    UObject object;

    public FloorImpl(UObject object) {
        this.object = object;
    }

    @Override
    public FloorButtonStatus getButtonStatus() {
        return (FloorButtonStatus) object.getProperty("buttonStatus");
    }

    @Override
    public void setButtonStatus(FloorButtonStatus status) {
        object.setProperty("buttonStatus", status);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addPeopleGroup(Group group) {
        List<Group> groups = (List<Group>) object.getProperty("peopleGroups");
        groups.add(group);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getPeopleGroups() {
        return (List<Group>) object.getProperty("peopleGroups");
    }
}

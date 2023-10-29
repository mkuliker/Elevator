package Objects;

import java.util.List;

public interface Floor {
    FloorButtonStatus getButtonStatus();
    void setButtonStatus(FloorButtonStatus status);
    void addPeopleGroup(Group group);
    List<Group> getPeopleGroups();
}

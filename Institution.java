import java.util.ArrayList;
import java.util.List;

public class Institution {
    private String institutionName;
    private String location;
    private List<Staff> staffList = new ArrayList<>();

    public Institution(String institutionName, String location) {
        this.institutionName = institutionName;
        this.location = location;
    }

    public String getInstitutionName() { return institutionName; }
    public String getLocation()        { return location; }
    public List<Staff> getStaffList()  { return staffList; }

    public void addStaff(Staff s) {
        for (Staff existing : staffList)
            if (existing.getStaffID().equalsIgnoreCase(s.getStaffID()))
                throw new IllegalArgumentException("Staff ID already exists: " + s.getStaffID());
        staffList.add(s);
    }

    public boolean removeStaff(String id) {
        return staffList.removeIf(s -> s.getStaffID().equalsIgnoreCase(id));
    }

    public Staff searchByID(String id) {
        for (Staff s : staffList)
            if (s.getStaffID().equalsIgnoreCase(id)) return s;
        return null;
    }

    public List<Staff> searchByName(String name) {
        List<Staff> result = new ArrayList<>();
        for (Staff s : staffList)
            if (s.getName().toLowerCase().contains(name.toLowerCase())) result.add(s);
        return result;
    }
}

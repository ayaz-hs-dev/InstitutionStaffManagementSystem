public class Administrator extends NonTeachingStaff {
    private String officeLocation;

    public Administrator(String staffID, String name, String department,
                         String role, String officeLocation) {
        super(staffID, name, department, role);
        this.officeLocation = officeLocation;
    }

    public String getOfficeLocation()        { return officeLocation; }
    public void setOfficeLocation(String o)  { this.officeLocation = o; }

    @Override
    public String displayDetails() {
        return "Administrator | " + getStaffID() + " | " + getName() + " | " +
               getDepartment() + " | " + getRole() + " | " + officeLocation;
    }
}

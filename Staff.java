public abstract class Staff {
    private final String staffID;
    private String name;
    private String department;

    public Staff(String staffID, String name, String department) {
        this.staffID = staffID;
        this.name = name;
        this.department = department;
    }

    public String getStaffID()    { return staffID; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }

    public void setName(String name)           { this.name = name; }
    public void setDepartment(String dept)     { this.department = dept; }

    public abstract String displayDetails();
}

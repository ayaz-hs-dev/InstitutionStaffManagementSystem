public class Technician extends NonTeachingStaff {
    private String technicalExpertise;

    public Technician(String staffID, String name, String department,
                      String role, String technicalExpertise) {
        super(staffID, name, department, role);
        this.technicalExpertise = technicalExpertise;
    }

    public String getTechnicalExpertise()        { return technicalExpertise; }
    public void setTechnicalExpertise(String t)  { this.technicalExpertise = t; }

    @Override
    public String displayDetails() {
        return "Technician | " + getStaffID() + " | " + getName() + " | " +
               getDepartment() + " | " + getRole() + " | " + technicalExpertise;
    }
}

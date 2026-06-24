public class Professor extends TeachingStaff {
    private String researchArea;

    public Professor(String staffID, String name, String department,
                     String subjectSpecialization, String researchArea) {
        super(staffID, name, department, subjectSpecialization);
        this.researchArea = researchArea;
    }

    public String getResearchArea()        { return researchArea; }
    public void setResearchArea(String r)  { this.researchArea = r; }

    @Override
    public String displayDetails() {
        return "Professor | " + getStaffID() + " | " + getName() + " | " +
               getDepartment() + " | " + getSubjectSpecialization() + " | " + researchArea;
    }
}

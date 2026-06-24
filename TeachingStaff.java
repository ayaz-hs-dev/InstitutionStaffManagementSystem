public abstract class TeachingStaff extends Staff {
    private String subjectSpecialization;

    public TeachingStaff(String staffID, String name, String department, String subjectSpecialization) {
        super(staffID, name, department);
        this.subjectSpecialization = subjectSpecialization;
    }

    public String getSubjectSpecialization()             { return subjectSpecialization; }
    public void setSubjectSpecialization(String s)       { this.subjectSpecialization = s; }
}

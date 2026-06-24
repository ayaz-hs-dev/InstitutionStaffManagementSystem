public class Lecturer extends TeachingStaff {
    private String courseLevel;

    public Lecturer(String staffID, String name, String department,
                    String subjectSpecialization, String courseLevel) {
        super(staffID, name, department, subjectSpecialization);
        this.courseLevel = courseLevel;
    }

    public String getCourseLevel()        { return courseLevel; }
    public void setCourseLevel(String c)  { this.courseLevel = c; }

    @Override
    public String displayDetails() {
        return "Lecturer | " + getStaffID() + " | " + getName() + " | " +
               getDepartment() + " | " + getSubjectSpecialization() + " | " + courseLevel;
    }
}

public abstract class NonTeachingStaff extends Staff {
    private String role;

    public NonTeachingStaff(String staffID, String name, String department, String role) {
        super(staffID, name, department);
        this.role = role;
    }

    public String getRole()        { return role; }
    public void setRole(String r)  { this.role = r; }
}

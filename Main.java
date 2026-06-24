import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {

    private Institution institution = new Institution("National University", "Islamabad");

    // Table
    private DefaultTableModel tableModel;

    // Add-form fields
    private JTextField tfID    = new JTextField(15);
    private JTextField tfName  = new JTextField(15);
    private JTextField tfDept  = new JTextField(15);
    private JTextField tfF1    = new JTextField(15);
    private JTextField tfF2    = new JTextField(15);
    private JComboBox<String> cbType = new JComboBox<>(
            new String[]{"Professor", "Lecturer", "Administrator", "Technician"});
    private JLabel lblF1 = new JLabel("Specialization:");
    private JLabel lblF2 = new JLabel("Research Area:");

    // Search
    private JTextField tfSearch = new JTextField(15);
    private JComboBox<String> cbSearchBy = new JComboBox<>(new String[]{"By ID", "By Name"});

    // Update
    private JTextField tfUid   = new JTextField(15);
    private JTextField tfUName = new JTextField(15);
    private JTextField tfUDept = new JTextField(15);

    // Remove
    private JTextField tfRid = new JTextField(15);

    // Status
    private JLabel lblStatus = new JLabel("Ready");

    public Main() {
        setTitle("Staff Management System - " + institution.getInstitutionName());
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("All Staff",  buildAllTab());
        tabs.addTab("Add Staff",  buildAddTab());
        tabs.addTab("Search",     buildSearchTab());
        tabs.addTab("Update",     buildUpdateTab());
        tabs.addTab("Remove",     buildRemoveTab());

        // Refresh table when switching to All Staff tab
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 0) refreshTable();
        });

        add(tabs, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        seedData();
        refreshTable();
        setVisible(true);
    }

    // Tab 1: All Staff
    private JPanel buildAllTab() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Name", "Department", "Type", "Field 1", "Field 2"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(22);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> refreshTable());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnRefresh);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    // Tab 2: Add Staff
    private JPanel buildAddTab() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.add(new JLabel("Staff ID:"));     form.add(tfID);
        form.add(new JLabel("Name:"));         form.add(tfName);
        form.add(new JLabel("Department:"));   form.add(tfDept);
        form.add(new JLabel("Staff Type:"));   form.add(cbType);
        form.add(lblF1);                       form.add(tfF1);
        form.add(lblF2);                       form.add(tfF2);

        cbType.addActionListener(e -> updateLabels());

        JButton btnAdd   = new JButton("Add Staff");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addStaff());
        btnClear.addActionListener(e -> clearForm());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnClear);
        buttons.add(btnAdd);

        p.add(form, BorderLayout.NORTH);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // Tab 3: Search
    private JPanel buildSearchTab() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        top.add(tfSearch);
        top.add(cbSearchBy);
        JButton btnSearch = new JButton("Search");
        JButton btnClear  = new JButton("Clear");
        top.add(btnSearch);
        top.add(btnClear);

        String[] cols = {"ID", "Name", "Department", "Type", "Field 1", "Field 2"};
        DefaultTableModel searchModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable searchTable = new JTable(searchModel);
        searchTable.setRowHeight(22);

        btnSearch.addActionListener(e -> {
            String q = tfSearch.getText().trim();
            if (q.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter a search term."); return; }
            searchModel.setRowCount(0);
            if (cbSearchBy.getSelectedIndex() == 0) {
                Staff s = institution.searchByID(q);
                if (s != null) addRow(searchModel, s);
                else JOptionPane.showMessageDialog(this, "No staff found with ID: " + q);
            } else {
                List<Staff> list = institution.searchByName(q);
                if (list.isEmpty()) JOptionPane.showMessageDialog(this, "No staff found with name: " + q);
                else list.forEach(s -> addRow(searchModel, s));
            }
            setStatus("Found " + searchModel.getRowCount() + " result(s).");
        });
        btnClear.addActionListener(e -> { searchModel.setRowCount(0); tfSearch.setText(""); });

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        return p;
    }

    // Tab 4: Update
    private JPanel buildUpdateTab() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Staff ID:")); form.add(tfUid);
        form.add(new JLabel("New Name (leave blank to skip):")); form.add(tfUName);
        form.add(new JLabel("New Department (leave blank to skip):")); form.add(tfUDept);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            String id = tfUid.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter a Staff ID."); return; }
            Staff s = institution.searchByID(id);
            if (s == null) { JOptionPane.showMessageDialog(this, "Staff not found: " + id); return; }
            if (!tfUName.getText().trim().isEmpty()) s.setName(tfUName.getText().trim());
            if (!tfUDept.getText().trim().isEmpty()) s.setDepartment(tfUDept.getText().trim());
            JOptionPane.showMessageDialog(this, "Updated!\n" + s.displayDetails());
            tfUid.setText(""); tfUName.setText(""); tfUDept.setText("");
            setStatus("Staff updated: " + s.getName());
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnUpdate);

        p.add(form, BorderLayout.NORTH);
        p.add(buttons, BorderLayout.CENTER);
        return p;
    }

    // Tab 5: Remove
    private JPanel buildRemoveTab() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Staff ID:"));
        form.add(tfRid);

        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(e -> {
            String id = tfRid.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter a Staff ID."); return; }
            Staff s = institution.searchByID(id);
            if (s == null) { JOptionPane.showMessageDialog(this, "Staff not found: " + id); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Remove " + s.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                institution.removeStaff(id);
                tfRid.setText("");
                refreshTable();
                setStatus("Removed: " + s.getName());
            }
        });

        form.add(btnRemove);
        p.add(form, BorderLayout.NORTH);
        return p;
    }

   

    private void addStaff() {
        String id   = tfID.getText().trim();
        String name = tfName.getText().trim();
        String dept = tfDept.getText().trim();
        String f1   = tfF1.getText().trim();
        String f2   = tfF2.getText().trim();

        if (id.isEmpty() || name.isEmpty() || dept.isEmpty() || f1.isEmpty() || f2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            Staff s;
            switch ((String) cbType.getSelectedItem()) {
                case "Professor":     s = new Professor(id, name, dept, f1, f2);     break;
                case "Lecturer":      s = new Lecturer(id, name, dept, f1, f2);      break;
                case "Administrator": s = new Administrator(id, name, dept, f1, f2); break;
                default:              s = new Technician(id, name, dept, f1, f2);    break;
            }
            institution.addStaff(s);
            JOptionPane.showMessageDialog(this, "Added: " + s.displayDetails());
            clearForm();
            refreshTable();
            setStatus("Staff added: " + name);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateLabels() {
        String type = (String) cbType.getSelectedItem();
        if (type.equals("Professor")) {
            lblF1.setText("Specialization:"); lblF2.setText("Research Area:");
        } else if (type.equals("Lecturer")) {
            lblF1.setText("Specialization:"); lblF2.setText("Course Level:");
        } else if (type.equals("Administrator")) {
            lblF1.setText("Role:"); lblF2.setText("Office Location:");
        } else {
            lblF1.setText("Role:"); lblF2.setText("Technical Expertise:");
        }
    }

    private void clearForm() {
        tfID.setText(""); tfName.setText(""); tfDept.setText("");
        tfF1.setText(""); tfF2.setText("");
        cbType.setSelectedIndex(0);
        updateLabels();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        institution.getStaffList().forEach(s -> addRow(tableModel, s));
        setStatus("Total staff: " + institution.getStaffList().size());
    }

    private void addRow(DefaultTableModel m, Staff s) {
        String f1 = "", f2 = "";
        if      (s instanceof Professor)     { f1 = ((Professor) s).getSubjectSpecialization();     f2 = ((Professor) s).getResearchArea(); }
        else if (s instanceof Lecturer)      { f1 = ((Lecturer) s).getSubjectSpecialization();      f2 = ((Lecturer) s).getCourseLevel(); }
        else if (s instanceof Administrator) { f1 = ((Administrator) s).getRole();                  f2 = ((Administrator) s).getOfficeLocation(); }
        else if (s instanceof Technician)    { f1 = ((Technician) s).getRole();                     f2 = ((Technician) s).getTechnicalExpertise(); }
        m.addRow(new Object[]{s.getStaffID(), s.getName(), s.getDepartment(),
                               s.getClass().getSimpleName(), f1, f2});
    }

    private void setStatus(String msg) { lblStatus.setText(" " + msg); }

    private void seedData() {
        try {
            institution.addStaff(new Professor("P001", "Dr. Ahmed Raza", "Computer Science", "AI", "Machine Learning"));
            institution.addStaff(new Professor("P002", "Dr. Sara Khan", "Mathematics", "Applied Math", "Numerical Analysis"));
            institution.addStaff(new Lecturer("L001", "Mr. Bilal Hassan", "Physics", "Quantum Mechanics", "Undergraduate"));
            institution.addStaff(new Lecturer("L002", "Ms. Aisha Noor", "CS", "Data Structures", "Postgraduate"));
            institution.addStaff(new Administrator("A001", "Mr. Usman Ali", "Admin", "Head of Admin", "Block A"));
            institution.addStaff(new Technician("T001", "Mr. Kamran Shah", "IT", "Network Tech", "Servers & Networking"));
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

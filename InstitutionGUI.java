import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InstitutionGUI extends JFrame {

    private static final Color CLR_BG       = new Color(245, 247, 250);
    private static final Color CLR_SIDEBAR  = new Color(30, 41, 59);
    private static final Color CLR_ACCENT   = new Color(59, 130, 246);
    private static final Color CLR_SUCCESS  = new Color(34, 197, 94);
    private static final Color CLR_DANGER   = new Color(239, 68, 68);
    private static final Color CLR_TEXT     = new Color(15, 23, 42);
    private static final Color CLR_MUTED    = new Color(100, 116, 139);
    private static final Color CLR_WHITE    = Color.WHITE;
    private static final Color CLR_HEADER   = new Color(248, 250, 252);

    private Institution institution = new Institution("National University", "Islamabad");

    private DefaultTableModel tableModel;
    private JTable staffTable;

    private JTextField tfID, tfName, tfDept, tfExtra1, tfExtra2;
    private JComboBox<String> cbType;
    private JLabel lblExtra1, lblExtra2;

    private JTextField tfSearch;
    private JComboBox<String> cbSearchType;

    private JLabel lblStatus;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public InstitutionGUI() {
        setTitle("Institution Staff Management System – " + institution.getInstitutionName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CLR_BG);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        seedSampleData();
        refreshTable(institution.getStaffList());
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(CLR_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Logo / title
        JLabel logo = new JLabel("Staff MS", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(CLR_WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        sidebar.add(logo);

        sidebar.add(sidebarBtn("All Staff",   "ALL"));
        sidebar.add(sidebarBtn("Add Staff",   "ADD"));
        sidebar.add(sidebarBtn("Search",      "SEARCH"));
        sidebar.add(sidebarBtn("Update",      "UPDATE"));
        sidebar.add(sidebarBtn("Remove",     "REMOVE"));

        sidebar.add(Box.createVerticalGlue());

        JLabel inst = new JLabel("<html><center>" + institution.getInstitutionName()
                + "<br><small>" + institution.getLocation() + "</small></center></html>",
                SwingConstants.CENTER);
        inst.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        inst.setForeground(CLR_MUTED);
        inst.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(inst);
        return sidebar;
    }

    private JButton sidebarBtn(String text, String card) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(CLR_WHITE);
        btn.setBackground(CLR_SIDEBAR);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 46));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(CLR_ACCENT); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(CLR_SIDEBAR); }
        });
        btn.addActionListener(e -> {
            cardLayout.show(cardPanel, card);
            if ("ALL".equals(card)) refreshTable(institution.getStaffList());
        });
        return btn;
    }
    private JPanel buildMainArea() {
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(CLR_BG);

        cardPanel.add(buildAllStaffPanel(), "ALL");
        cardPanel.add(buildAddPanel(),      "ADD");
        cardPanel.add(buildSearchPanel(),   "SEARCH");
        cardPanel.add(buildUpdatePanel(),   "UPDATE");
        cardPanel.add(buildRemovePanel(),   "REMOVE");

        cardLayout.show(cardPanel, "ALL");
        return cardPanel;
    }


    private JPanel buildAllStaffPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(CLR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(pageTitle("All Staff Members"), BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Department", "Type", "Detail 1", "Detail 2"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        staffTable = new JTable(tableModel);
        styleTable(staffTable);

        JScrollPane sp = new JScrollPane(staffTable);
        sp.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        p.add(sp, BorderLayout.CENTER);

        JButton btnRefresh = styledBtn("Refresh", CLR_ACCENT);
        btnRefresh.addActionListener(e -> refreshTable(institution.getStaffList()));
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(CLR_BG);
        south.add(btnRefresh);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    
    private JPanel buildAddPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(CLR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(pageTitle("Add New Staff Member"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CLR_WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 8, 7, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        tfID   = new JTextField(18);
        tfName = new JTextField(18);
        tfDept = new JTextField(18);
        String[] types = {"Professor", "Lecturer", "Administrator", "Technician"};
        cbType = new JComboBox<>(types);
        tfExtra1 = new JTextField(18);
        tfExtra2 = new JTextField(18);
        lblExtra1 = new JLabel("Subject Specialization:");
        lblExtra2 = new JLabel("Research Area:");

        cbType.addActionListener(e -> updateDynamicLabels());

        addFormRow(form, gc, 0, "Staff ID:",    tfID);
        addFormRow(form, gc, 1, "Full Name:",   tfName);
        addFormRow(form, gc, 2, "Department:",  tfDept);
        addFormRow(form, gc, 3, "Staff Type:",  cbType);
        addFormRow(form, gc, 4, lblExtra1,      tfExtra1);
        addFormRow(form, gc, 5, lblExtra2,      tfExtra2);

        styleFormFields(tfID, tfName, tfDept, tfExtra1, tfExtra2);
        updateDynamicLabels();

        p.add(form, BorderLayout.CENTER);

        JButton btnAdd = styledBtn("Add Staff", CLR_SUCCESS);
        btnAdd.addActionListener(e -> handleAddStaff());
        JButton btnClear = styledBtn("Clear", CLR_MUTED);
        btnClear.addActionListener(e -> clearAddForm());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        south.setBackground(CLR_BG);
        south.add(btnClear);
        south.add(btnAdd);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    
    private JPanel buildSearchPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(CLR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(pageTitle("Search Staff"), BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(CLR_BG);
        tfSearch    = new JTextField(22);
        cbSearchType = new JComboBox<>(new String[]{"By ID", "By Name"});
        styleFormFields(tfSearch);
        JButton btnSearch = styledBtn("Search", CLR_ACCENT);
        JButton btnClearS = styledBtn("Clear", CLR_MUTED);
        top.add(new JLabel("Search:"));
        top.add(tfSearch);
        top.add(cbSearchType);
        top.add(btnSearch);
        top.add(btnClearS);
        p.add(top, BorderLayout.CENTER);

        // Results table (reuse same model structure)
        String[] cols = {"ID", "Name", "Department", "Type", "Detail 1", "Detail 2"};
        DefaultTableModel searchModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable searchTable = new JTable(searchModel);
        styleTable(searchTable);
        JScrollPane sp = new JScrollPane(searchTable);
        sp.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(CLR_BG);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bottom.add(sp, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        // Give it some height
        bottom.setPreferredSize(new Dimension(0, 380));

        btnSearch.addActionListener(e -> {
            String query = tfSearch.getText().trim();
            if (query.isEmpty()) { showError("Please enter a search term."); return; }
            searchModel.setRowCount(0);
            if ("By ID".equals(cbSearchType.getSelectedItem())) {
                Staff s = institution.searchByID(query);
                if (s != null) addRowToModel(searchModel, s);
                else showInfo("No staff found with ID: " + query);
            } else {
                List<Staff> results = institution.searchByName(query);
                if (results.isEmpty()) showInfo("No staff found with name containing: " + query);
                else results.forEach(s -> addRowToModel(searchModel, s));
            }
            showStatus("Search complete – " + searchModel.getRowCount() + " result(s).");
        });
        btnClearS.addActionListener(e -> { searchModel.setRowCount(0); tfSearch.setText(""); });
        return p;
    }

    
    private JPanel buildUpdatePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(CLR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(pageTitle("Update Staff Details"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CLR_WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 8, 7, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfUName = new JTextField(18);
        JTextField tfUDept = new JTextField(18);
        styleFormFields(tfUUID, tfUName, tfUDept);

        addFormRow(form, gc, 0, "Staff ID (to update):", tfUUID);
        addFormRow(form, gc, 1, "New Name (leave blank to keep):", tfUName);
        addFormRow(form, gc, 2, "New Department (leave blank to keep):", tfUDept);

        p.add(form, BorderLayout.CENTER);

        JButton btnUpdate = styledBtn("Apply Update", CLR_ACCENT);
        btnUpdate.addActionListener(e -> {
            String id = tfUUID.getText().trim();
            if (id.isEmpty()) { showError("Please enter a Staff ID."); return; }
            Staff s = institution.searchByID(id);
            if (s == null) { showError("No staff found with ID: " + id); return; }
            try {
                if (!tfUName.getText().trim().isEmpty()) s.setName(tfUName.getText().trim());
                if (!tfUDept.getText().trim().isEmpty()) s.setDepartment(tfUDept.getText().trim());
                showStatus("Staff '" + s.getName() + "' updated successfully.");
                showInfo("Update successful!\n" + s.displayDetails());
                tfUUID.setText(""); tfUName.setText(""); tfUDept.setText("");
                refreshTable(institution.getStaffList());
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(CLR_BG);
        south.add(btnUpdate);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    // TEMP field references for update panel (need to be accessible in lambda)
    private JTextField tfUUID = new JTextField(18);

    private JPanel buildRemovePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(CLR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(pageTitle("Remove Staff Member"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CLR_WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 8, 7, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfRID = new JTextField(18);
        styleFormFields(tfRID);
        addFormRow(form, gc, 0, "Staff ID to Remove:", tfRID);
        p.add(form, BorderLayout.CENTER);

        JButton btnRemove = styledBtn("Remove Staff", CLR_DANGER);
        btnRemove.addActionListener(e -> {
            String id = tfRID.getText().trim();
            if (id.isEmpty()) { showError("Please enter a Staff ID."); return; }
            Staff s = institution.searchByID(id);
            if (s == null) { showError("No staff found with ID: " + id); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Remove staff member: " + s.getName() + " (" + id + ")?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                institution.removeStaff(id);
                showStatus("Staff member '" + s.getName() + "' removed.");
                showInfo("Removed: " + s.getName());
                tfRID.setText("");
                refreshTable(institution.getStaffList());
            }
        });
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(CLR_BG);
        south.add(btnRemove);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(CLR_SIDEBAR);
        bar.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));
        lblStatus = new JLabel("Ready.");
        lblStatus.setForeground(CLR_MUTED);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bar.add(lblStatus, BorderLayout.WEST);
        return bar;
    }


    private void handleAddStaff() {
        try {
            String id   = tfID.getText().trim();
            String name = tfName.getText().trim();
            String dept = tfDept.getText().trim();
            String e1   = tfExtra1.getText().trim();
            String e2   = tfExtra2.getText().trim();

            if (id.isEmpty() || name.isEmpty() || dept.isEmpty() || e1.isEmpty() || e2.isEmpty()) {
                showError("All fields are required.");
                return;
            }
            if (!id.matches("[A-Za-z0-9\\-]+")) {
                showError("Staff ID may only contain letters, digits, or hyphens.");
                return;
            }

            Staff s;
            switch ((String) cbType.getSelectedItem()) {
                case "Professor":     s = new Professor(id, name, dept, e1, e2);     break;
                case "Lecturer":      s = new Lecturer(id, name, dept, e1, e2);      break;
                case "Administrator": s = new Administrator(id, name, dept, e1, e2); break;
                default:              s = new Technician(id, name, dept, e1, e2);    break;
            }
            institution.addStaff(s);
            showStatus("Staff member '" + name + "' added successfully.");
            showInfo("Added!\n" + s.displayDetails());
            clearAddForm();
            refreshTable(institution.getStaffList());
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateDynamicLabels() {
        String type = (String) cbType.getSelectedItem();
        switch (type) {
            case "Professor":
                lblExtra1.setText("Subject Specialization:");
                lblExtra2.setText("Research Area:");
                break;
            case "Lecturer":
                lblExtra1.setText("Subject Specialization:");
                lblExtra2.setText("Course Level (e.g. Undergraduate):");
                break;
            case "Administrator":
                lblExtra1.setText("Role:");
                lblExtra2.setText("Office Location:");
                break;
            case "Technician":
                lblExtra1.setText("Role:");
                lblExtra2.setText("Technical Expertise:");
                break;
        }
    }

    private void clearAddForm() {
        tfID.setText(""); tfName.setText(""); tfDept.setText("");
        tfExtra1.setText(""); tfExtra2.setText("");
        cbType.setSelectedIndex(0);
        updateDynamicLabels();
    }

    private void refreshTable(List<Staff> list) {
        tableModel.setRowCount(0);
        list.forEach(s -> addRowToModel(tableModel, s));
        showStatus("Showing " + list.size() + " staff record(s).");
    }

    private void addRowToModel(DefaultTableModel m, Staff s) {
        String type = s.getClass().getSimpleName();
        String d1 = "", d2 = "";
        if (s instanceof Professor)     { d1 = ((Professor) s).getSubjectSpecialization();   d2 = ((Professor) s).getResearchArea(); }
        else if (s instanceof Lecturer) { d1 = ((Lecturer) s).getSubjectSpecialization();    d2 = ((Lecturer) s).getCourseLevel(); }
        else if (s instanceof Administrator) { d1 = ((Administrator) s).getRole();           d2 = ((Administrator) s).getOfficeLocation(); }
        else if (s instanceof Technician)    { d1 = ((Technician) s).getRole();              d2 = ((Technician) s).getTechnicalExpertise(); }
        m.addRow(new Object[]{s.getStaffID(), s.getName(), s.getDepartment(), type, d1, d2});
    }

    private JLabel pageTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l.setForeground(CLR_TEXT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return l;
    }

    private JButton styledBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(CLR_WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTable(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(30);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(CLR_HEADER);
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setGridColor(new Color(226, 232, 240));
        t.setShowGrid(true);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void styleFormFields(JTextField... fields) {
        for (JTextField f : fields) {
            f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225)),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gc, int row,
                             Object labelOrComp, JComponent field) {
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        if (labelOrComp instanceof String)
            panel.add(labeledText((String) labelOrComp), gc);
        else
            panel.add((Component) labelOrComp, gc);
        gc.gridx = 1; gc.weightx = 1;
        panel.add(field, gc);
    }

    private JLabel labeledText(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(CLR_TEXT);
        return l;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        showStatus("⚠  " + msg);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStatus(String msg) {
        lblStatus.setText(msg);
    }

    private void seedSampleData() {
        try {
            institution.addStaff(new Professor("P001", "Dr. Ahmed Raza", "Computer Science",
                    "Artificial Intelligence", "Machine Learning & NLP"));
            institution.addStaff(new Professor("P002", "Dr. Sara Khan", "Mathematics",
                    "Applied Mathematics", "Numerical Analysis"));
            institution.addStaff(new Lecturer("L001", "Mr. Bilal Hassan", "Physics",
                    "Quantum Mechanics", "Undergraduate"));
            institution.addStaff(new Lecturer("L002", "Ms. Aisha Noor", "Computer Science",
                    "Data Structures", "Postgraduate"));
            institution.addStaff(new Administrator("A001", "Mr. Usman Ali", "Admin Office",
                    "Head of Administration", "Block A – Room 101"));
            institution.addStaff(new Administrator("A002", "Ms. Fatima Malik", "Finance",
                    "Finance Manager", "Block B – Room 205"));
            institution.addStaff(new Technician("T001", "Mr. Kamran Shah", "IT Department",
                    "Network Technician", "Network Infrastructure & Servers"));
            institution.addStaff(new Technician("T002", "Mr. Zubair Ahmed", "Labs",
                    "Lab Technician", "Electronic Instruments & Equipment"));
        } catch (Exception ignored) {}
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new InstitutionGUI();
        });
    }
}

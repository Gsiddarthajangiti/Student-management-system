import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StudentForm extends JDialog {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField courseField;
    private JButton saveButton;
    private JButton cancelButton;
    private DatabaseManager dbManager;
    private Student student;

    public StudentForm(Frame parent, String title, Student student) {
        super(parent, title, true);
        this.student = student;
        try {
            dbManager = new DatabaseManager();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            System.exit(1);
        }

        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Course:"));
        courseField = new JTextField();
        panel.add(courseField);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);

        if (student != null) {
            nameField.setText(student.getName());
            ageField.setText(String.valueOf(student.getAge()));
            courseField.setText(student.getCourse());
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void saveStudent() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String course = courseField.getText();

        try {
            if (student == null) {
                dbManager.addStudent(new Student(0, name, age, course));
            } else {
                student.setName(name);
                student.setAge(age);
                student.setCourse(course);
                dbManager.updateStudent(student);
            }
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving student: " + e.getMessage());
        }
    }
}

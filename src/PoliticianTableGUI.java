import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PoliticianTableGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PoliticianTableGUI("src/Politicians where gender equals female.csv"); // Update file path
        });
    }

    public PoliticianTableGUI(String filePath) {
        // Load data
        List<Politician> politicians = PoliticianDataReader.loadData(filePath);

        // Create frame
        JFrame frame = new JFrame("Politician Data Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center window

        // Table columns
        String[] columnNames = { "Name", "Country", "Birth Year", "Party" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add data to table
        for (Politician p : politicians) {
            model.addRow(new Object[]{ p.getName(), p.getCountry(), p.getBirthYear(), p.getParty() });
        }

        // Create JTable
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Customize table
        table.setAutoCreateRowSorter(true); // Enable sorting
        table.setFillsViewportHeight(true);

        // Add components to frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Show window
        frame.setVisible(true);
    }
}

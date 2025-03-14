import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PoliticianGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel detailsLabel;
    private JLabel statsLabel;
    private ChartPanel chartPanel;
    private JComboBox<String> filterComboBox;
    private List<Politician> politicians;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PoliticianGUI("src/Politicians where gender equals female.csv"));
    }

    public PoliticianGUI(String filePath) {
        politicians = PoliticianDataReader.loadData(filePath);

        // Create main frame
        frame = new JFrame("Politician Data Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // Create UI panels
        createTablePanel();
        createStatsPanel();
        createChartPanel();
        createFilterPanel();

        // Show window
        frame.setVisible(true);
    }

    private void createTablePanel() {
        String[] columns = { "Name", "Country", "Birth Year", "Party" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);

        // Add data to table
        updateTable(politicians);

        // Add listener to update Details Panel on selection
        table.getSelectionModel().addListSelectionListener(this::updateDetailsPanel);

        // Add table to frame
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void createStatsPanel() {
        statsLabel = new JLabel("Stats: Total Politicians = " + politicians.size());
        detailsLabel = new JLabel("Select a politician to see details.");

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 1)); // Use grid layout to show both labels
        statsPanel.add(statsLabel);
        statsPanel.add(detailsLabel);

        frame.add(statsPanel, BorderLayout.NORTH);
    }

    private void createChartPanel() {
        chartPanel = new ChartPanel(createChart(politicians));
        frame.add(chartPanel, BorderLayout.EAST);
    }

    private void createFilterPanel() {
        // Collect all unique countries dynamically
        Set<String> countrySet = politicians.stream()
                .map(Politician::getCountry)
                .collect(Collectors.toSet());

        filterComboBox = new JComboBox<>(countrySet.toArray(new String[0]));
        filterComboBox.insertItemAt("All", 0);
        filterComboBox.setSelectedIndex(0);

        filterComboBox.addActionListener(e -> applyFilter());

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter by Country:"));
        filterPanel.add(filterComboBox);
        frame.add(filterPanel, BorderLayout.SOUTH);
    }

    private void updateTable(List<Politician> filteredList) {
        tableModel.setRowCount(0);
        for (Politician p : filteredList) {
            tableModel.addRow(new Object[]{ p.getName(), p.getCountry(), p.getBirthYear(), p.getParty() });
        }
    }

    private void updateDetailsPanel(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            int row = table.convertRowIndexToModel(table.getSelectedRow()); // Convert to model index
            String name = (String) tableModel.getValueAt(row, 0);
            String country = (String) tableModel.getValueAt(row, 1);
            String birthYear = String.valueOf(tableModel.getValueAt(row, 2)); // Fix: Convert int safely
            String party = (String) tableModel.getValueAt(row, 3);

            detailsLabel.setText("<html><b>Name:</b> " + name + "<br><b>Country:</b> " + country +
                    "<br><b>Birth Year:</b> " + birthYear + "<br><b>Party:</b> " + party + "</html>");
        }
    }

    private JFreeChart createChart(List<Politician> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Long> countByCountry = data.stream()
                .collect(Collectors.groupingBy(Politician::getCountry, Collectors.counting()));

        for (Map.Entry<String, Long> entry : countByCountry.entrySet()) {
            dataset.addValue(entry.getValue(), "Politicians", entry.getKey());
        }

        return ChartFactory.createBarChart("Politicians by Country", "Country", "Count", dataset);
    }

    private void applyFilter() {
        String selectedCountry = (String) filterComboBox.getSelectedItem();
        List<Politician> filteredList = "All".equals(selectedCountry)
                ? politicians
                : politicians.stream().filter(p -> p.getCountry().equals(selectedCountry)).collect(Collectors.toList());

        // Update table
        updateTable(filteredList);

        // Update stats
        statsLabel.setText("Stats: Total Politicians = " + filteredList.size());

        // Update chart efficiently
        chartPanel.setChart(createChart(filteredList));
    }
}

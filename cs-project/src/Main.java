import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void createAndShowGUI() throws IOException {
        JFrame frame = new JFrame("LoL Champions");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel topPanel = new JPanel();
        JButton topButton = new JButton("Top");
        JButton jungleButton = new JButton("Jungle");
        JButton midButton = new JButton("Mid");
        JButton adcButton = new JButton("ADC");
        JButton supportButton = new JButton("Support");

        topPanel.add(topButton);
        topPanel.add(jungleButton);
        topPanel.add(midButton);
        topPanel.add(adcButton);
        topPanel.add(supportButton);

        JTable table = new JTable();
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String csvFile;
                if (e.getSource() == topButton) {
                    csvFile = "top.csv";
                } else if (e.getSource() == jungleButton) {
                    csvFile = "jungle.csv";
                } else if (e.getSource() == midButton) {
                    csvFile = "mid.csv";
                } else if (e.getSource() == adcButton) {
                    csvFile = "adc.csv";
                } else {
                    csvFile = "support.csv";
                }
                updateTable(csvFile, table);
            }
        };

        topButton.addActionListener(buttonListener);
        jungleButton.addActionListener(buttonListener);
        midButton.addActionListener(buttonListener);
        adcButton.addActionListener(buttonListener);
        supportButton.addActionListener(buttonListener);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void updateTable(String csvFile, JTable table) {
        String[] columnNames = {"Rank", "Champion", "Tier", "Win Rate", "Pick Rate", "Ban Rate", "Weak Against"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip the first line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                tableModel.addRow(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setModel(tableModel);
    }
}

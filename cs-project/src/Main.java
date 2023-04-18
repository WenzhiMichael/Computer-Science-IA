import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Top Champions Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Rank", "Champion", "Tier", "Win Rate", "Pick Rate", "Ban Rate", "Weak Against"}, 0);

        // Read the data from the CSV file and add it to the table model
        String csvFile = "top.csv";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the first line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                model.addRow(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("top.csv"))) {
            while ((line = br.readLine()) != null) {
                // Process the line...
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model);

        // Set the row height to a larger value
        table.setRowHeight(40);

        // Set the color of the grid lines to black
        table.setGridColor(Color.BLACK);

        // Show horizontal and vertical grid lines
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        // Set the font of the table cells to be bigger
        Font font = new Font("Arial", Font.PLAIN, 18);
        table.setFont(font);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Champions Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Rank", "Champion", "Tier", "Win Rate", "Pick Rate", "Ban Rate", "Weak Against"}, 0);

        try (BufferedReader br = new BufferedReader(new FileReader("top.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                model.addRow(values);
                System.out.println("Added row: " + String.join(", ", values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        System.out.println("Table rows: " + model.getRowCount());
        System.out.println("Table columns: " + model.getColumnCount());

        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}

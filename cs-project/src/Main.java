import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Main {

    private static boolean isAscending = true;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Update the addTableHeaderMouseListener method like this
    private static void addTableHeaderMouseListener(JTable table) {
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = table.columnAtPoint(e.getPoint());
                if (columnIndex >= 0 && columnIndex < 6) { // Change this value to 6 to include the last column
                    sortTableData(table, columnIndex);
                    isAscending = !isAscending; // Toggle the sorting direction
                }
            }
        });
    }

    private static JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Load the image and create the JLabel
        ImageIcon imageIcon = new ImageIcon("12.png"); // Replace with the path to your image file
        Image scaledImage = imageIcon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the welcome message JLabel
        String welcomeMessage = "<html><div style='text-align: center;'>Welcome to the League of Legends! Get ready to choose your champion and battle it out with opponents in exciting matches that will test your strategy and skills. Whether you're a seasoned player or new to the game, there's always something new to explore and discover. Let's dive into the action and see where your journey takes you!</div></html>";
        JLabel welcomeLabel = new JLabel(welcomeMessage);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        homePanel.add(imageLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        homePanel.add(welcomeLabel);

        return homePanel;
    }

    // Modify the sortTableData method like this
    private static void sortTableData(JTable table, int columnIndex) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowCount = tableModel.getRowCount();
        ArrayList<Object[]> rows = new ArrayList<>();

        // Save the current table data
        for (int i = 0; i < rowCount; i++) {
            Object[] row =

                    new Object[6];
            for (int j = 0; j < 6; j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            rows.add(row);
        }

        // Sort the data based on the column index
        rows.sort((a, b) -> {
            int comparison;
            switch (columnIndex) {
                case 0:
                    comparison = ((String) a[columnIndex]).compareTo((String) b[columnIndex]);
                    break;
                case 1:
                    comparison = ((String) a[columnIndex]).compareTo((String) b[columnIndex]);
                    break;
                case 2:
                case 3:
                case 4:
                    comparison = Double.compare(Double.parseDouble(((String) b[columnIndex]).substring(0, ((String) b[columnIndex]).length() - 1)), Double.parseDouble(((String) a[columnIndex]).substring(0, ((String) a[columnIndex]).length() - 1)));
                    break;
                default:
                    comparison = 0;
            }
            return isAscending ? comparison : -comparison;
        });

        // Update the table with the sorted data
        tableModel.setRowCount(0);
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    private static void createAndShowGUI() throws IOException {
        JFrame frame = new JFrame("LoL Champions");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
        JButton homeButton = new JButton("Home");
        JButton topButton = new JButton("Top");
        JButton jungleButton = new JButton("Jungle");
        JButton midButton = new JButton("Mid");
        JButton adcButton = new JButton("ADC");
        JButton supportButton = new JButton("Support");

        JButton[] buttons = {homeButton, topButton, jungleButton, midButton, adcButton, supportButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.setForeground(Color.ORANGE);
            topPanel.add(button);
        }

        JPanel contentPanel = new JPanel(new CardLayout());
        JPanel homePanel = createHomePanel();
        JTable table = new JTable();
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setForeground(Color.ORANGE);
        table.setBackground(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(table);

        // Custom table header with red inverted triangle
        JTableHeader tableHeader = new JTableHeader(table.getColumnModel()) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.RED);

                int triangleSize = 15;
                int[] xPoints = new int[]{0, -triangleSize, 0};
                int[] yPoints = new int[]{5, 5, 5 + triangleSize};

                for (int i = 0; i < 5; i++) { // Change this value to 4 to exclude the last column
                    Rectangle rect = getHeaderRect(i);
                    g2d.translate(rect.x + rect.width - 15, 0); // Move the triangle to the rightmost edge of the column
                    g2d.fillPolygon(xPoints, yPoints, 3);
                    g2d.translate(-(rect.x + rect.width - 15), 0);
                }
                g2d.dispose();
            }
        };


        table.setTableHeader(tableHeader);
        tableHeader.setFont(new Font("Arial", Font.PLAIN, 16));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(Color.BLACK);

        contentPanel.add(homePanel, "Home");
        contentPanel.add(scrollPane, "Table");

        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

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
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "Table");
            }
        };

        topButton.addActionListener(buttonListener);
        jungleButton.addActionListener(buttonListener);
        midButton.addActionListener(buttonListener);
        adcButton.addActionListener(buttonListener);
        supportButton.addActionListener(buttonListener);

        // Add this line after creating the table
        addTableHeaderMouseListener(table);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "Home");
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static void updateTable(String csvFile, JTable table) {
        String[] columnNames = { "Champion", "Tier", "Win Rate", "Pick Rate", "Ban Rate", "Weak Against"};
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
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(Color.BLACK);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(50); // Tier
        table.getColumnModel().getColumn(2).setPreferredWidth(80); // Win Rate
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // Pick Rate
        table.getColumnModel().getColumn(4).setPreferredWidth(80); // Ban Rate
    }


}
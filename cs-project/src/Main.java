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
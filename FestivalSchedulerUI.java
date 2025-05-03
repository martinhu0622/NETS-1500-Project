import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class FestivalSchedulerUI extends JFrame {

    private Set<String> selectedArtists = new LinkedHashSet<>();
    private JTextArea resultArea;
    private JLabel artistsDisplay, genreDisplay;
    private java.util.List<Performance> allPerformances;

    public FestivalSchedulerUI(java.util.List<Performance> performances) {
        this.allPerformances = performances;

        setTitle("Festival Scheduler");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(135, 206, 235);

        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel artistLabel = new JLabel("Double-click to pick 3 Artists:");
        artistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(artistLabel);

        if (allPerformances == null || allPerformances.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No performances loaded. Please check your data file.");
            System.exit(1);
        }

        // Build artist list from performances
        Set<String> artistNamesSet = new LinkedHashSet<>();
        for (Performance p : allPerformances) {
            if (p.artist != null && !p.artist.isEmpty()) {
                artistNamesSet.add(p.artist);
            }
        }
        String[] artistNames = artistNamesSet.toArray(new String[0]);

        JList<String> artistList = new JList<>(artistNames);
        artistList.setVisibleRowCount(6);
        artistList.setFixedCellHeight(30);
        artistList.setBackground(Color.WHITE);
        panel.add(new JScrollPane(artistList));

        artistsDisplay = new JLabel("Selected: ");
        artistsDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(artistsDisplay);

        JLabel genreLabel = new JLabel("Pick Your Genre:");
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(genreLabel);

        // Build genre list from performances
        Set<String> genres = new LinkedHashSet<>();
        for (Performance p : allPerformances) {
            if (p.genre != null && !p.genre.isEmpty()) {
                genres.add(p.genre);
            }
        }
        JComboBox<String> genreBox = new JComboBox<>(genres.toArray(new String[0]));
        genreBox.setMaximumSize(new Dimension(200, 30));
        panel.add(genreBox);

        genreDisplay = new JLabel("Genre: ");
        genreDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(genreDisplay);

        JButton genButton = new JButton("Generate Schedule");
        genButton.setBackground(btnColor);
        genButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(genButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(btnColor);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(resetButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBackground(Color.WHITE);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane resultScroll = new JScrollPane(resultArea);

        add(panel, BorderLayout.NORTH);
        add(resultScroll, BorderLayout.CENTER);

        // Logic
        artistList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String val = artistList.getSelectedValue();
                if (e.getClickCount() == 2 && val != null && selectedArtists.size() < 3) {
                    if (!selectedArtists.contains(val)) {
                        selectedArtists.add(val);
                        artistsDisplay.setText("Selected: " + String.join(", ", selectedArtists));
                    }
                }
            }
        });

        genreBox.addActionListener(e -> genreDisplay.setText("Genre: " + genreBox.getSelectedItem()));

        resetButton.addActionListener(e -> {
            artistList.clearSelection();
            genreBox.setSelectedIndex(0);
            selectedArtists.clear();
            artistsDisplay.setText("Selected: ");
            resultArea.setText("");
        });

        genButton.addActionListener(e -> {
            if (selectedArtists.size() != 3) {
                JOptionPane.showMessageDialog(this, "Pick exactly 3 artists.");
                return;
            }

            String selectedGenre = (String) genreBox.getSelectedItem();
            Set<String> genreSet = new HashSet<>(Collections.singleton(selectedGenre));

            ScheduleOptimizer optimizer = new ScheduleOptimizer(selectedArtists, genreSet);
            Performance start = new Performance("Start", "", 0, 0, 0, 0);
            java.util.List<Performance> schedule = optimizer.bfsSchedule(allPerformances, start);
            double finalScore = 0;
            for (Performance p : schedule) {
                finalScore += optimizer.getScoreBetween(start, p);
            }

            String artistScores = "\nSelected Artist Scores:\n";
            for (String artist : selectedArtists) {
                for (Performance p : allPerformances) {
                    if (p.artist.equals(artist)) {
                        double backendScore = optimizer.getScoreBetween(start, p);
                        artistScores += artist + " → Score: " + String.format("%.2f", backendScore) + "\n";
                        break; // Stop after finding the first match
                    }
                }
            }

            String scheduleText = "Schedule:\n\n";
            for (Performance p : schedule) {
                scheduleText += String.format("%02d:%02d", p.startTime / 60, p.startTime % 60)
                        + " - " + p.artist + " (" + p.genre + ")\n";
            }

            scheduleText += artistScores;
            scheduleText += "\nFinal Total Score: " + String.format("%.2f", finalScore) + "\n";

            resultArea.setText(scheduleText);
        });
    }
        private static String formatTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    public static void main(String[] args) {
        String path = "src/netsPerformanceSampleData.csv";
        try {
            java.util.List<Performance> performances = PerformanceParser.parseCSV(path);
            SwingUtilities.invokeLater(() -> new FestivalSchedulerUI(performances).setVisible(true));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load performance data.");
        }
    }
}




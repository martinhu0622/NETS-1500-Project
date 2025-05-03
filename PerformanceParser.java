import java.io.*;
import java.util.*;

public class PerformanceParser {

    public static List<Performance> parseCSV(String filePath) throws IOException {
        List<Performance> performances = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine(); // skip header

        while ((line = reader.readLine()) != null) {
            // Split only on commas that are not inside quotes
            String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            String artist = tokens[0].trim();
            String time = tokens[1].trim();
            String[] coord = tokens[2].replace("\"", "").split(",");
            double x = Double.parseDouble(coord[0].trim());
            double y = Double.parseDouble(coord[1].trim());
            String genre = tokens[3].trim();

            int start = convertTimeToMinutes(time);
            int end = start + 60;

            performances.add(new Performance(artist, genre, x, y, start, end));
        }

        reader.close();
        return performances;
    }

    private static int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}

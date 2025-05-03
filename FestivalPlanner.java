import java.io.*;
import java.util.*;

public class FestivalPlanner {
    public static void main(String[] args) throws IOException {
        Set<String> favoriteArtists = new HashSet<>(Arrays.asList("Stella Moon", "Kairos Blue"));
        Set<String> favoriteGenres = new HashSet<>(Arrays.asList("Ambient Electronica", "Indie Pop"));

        List<Performance> allPerformances = PerformanceParser.parseCSV("/Users/martinhu0622/Desktop/NETS 1500 Project/src/netsPerformanceSampleData.csv");

        // Dummy node
        Performance start = new Performance("Start", "", 0, 0, 0, 0);

        ScheduleOptimizer optimizer = new ScheduleOptimizer(favoriteArtists, favoriteGenres);
        List<Performance> schedule = optimizer.bfsSchedule(allPerformances, start);

        for (Performance p : schedule) {
            System.out.println(p.artist + " at " + formatTime(p.startTime) + " (" + p.genre + ")");
        }
    }

    private static String formatTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}


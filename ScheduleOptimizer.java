import java.util.*;

public class ScheduleOptimizer {

    private Set<String> favoriteArtists;
    private Set<String> favoriteGenres;

    public ScheduleOptimizer(Set<String> favoriteArtists, Set<String> favoriteGenres) {
        this.favoriteArtists = favoriteArtists;
        this.favoriteGenres = favoriteGenres;
    }

    private double score(Performance from, Performance to) {
        double distance = from.distanceTo(to);
        double preferenceBoost = 0;

        if (favoriteArtists.contains(to.artist)) {
            preferenceBoost += 1000; // Big bonus
        }
        if (favoriteGenres.contains(to.genre)) {
            preferenceBoost += 200; // Medium bonus
        }

        // Lower score is better
        return distance - preferenceBoost;
    }

    public List<Performance> bfsSchedule(List<Performance> allPerformances, Performance startNode) {
        // Group performances by start time (sorted)
        Map<Integer, List<Performance>> byStartTime = new TreeMap<>();
        for (Performance p : allPerformances) {
            byStartTime.computeIfAbsent(p.startTime, k -> new ArrayList<>()).add(p);
        }

        List<Performance> schedule = new ArrayList<>();
        Queue<Performance> queue = new LinkedList<>();
        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Performance current = queue.poll();
            int nextTime = Integer.MAX_VALUE;

            // Find the next available start time
            for (int time : byStartTime.keySet()) {
                if (time >= current.endTime) {
                    nextTime = time;
                    break;
                }
            }

            if (nextTime == Integer.MAX_VALUE) break;

            List<Performance> candidates = byStartTime.get(nextTime);
            Performance bestNext = null;
            double bestScore = Double.MAX_VALUE;

            for (Performance candidate : candidates) {
                double s = score(current, candidate);
                if (s < bestScore) {
                    bestScore = s;
                    bestNext = candidate;
                }
            }

            if (bestNext != null) {
                schedule.add(bestNext);
                queue.offer(bestNext);
            }
        }

        return schedule;
    }

    public double getScoreBetween(Performance from, Performance to) {
        return score(from, to);
    }
}



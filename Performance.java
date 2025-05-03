class Performance {
    String artist;
    String genre;
    double x, y;
    int startTime, endTime;

    public Performance(String artist, String genre, double x, double y, int startTime, int endTime) {
        this.artist = artist;
        this.genre = genre;
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double distanceTo(Performance other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}

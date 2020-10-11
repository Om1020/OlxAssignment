package apibuilder;

public class IMDBMovieDetail implements Comparable<IMDBMovieDetail> {

    double imdbRating;
    String tiltle;

    public IMDBMovieDetail(double imdbRating, String tiltle) {
        this.imdbRating = imdbRating;
        this.tiltle = tiltle;
    }

    public int compareTo(IMDBMovieDetail imdbMovieDetail) {
        if (imdbMovieDetail == null)
            return 0;

        return -(this.imdbRating > imdbMovieDetail.imdbRating ? 1 : imdbMovieDetail.imdbRating > this.imdbRating ? -1 : 0);
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public String getTiltle() {
        return tiltle;
    }

    public String toString() {
        return this.imdbRating + " " + this.tiltle;
    }
}

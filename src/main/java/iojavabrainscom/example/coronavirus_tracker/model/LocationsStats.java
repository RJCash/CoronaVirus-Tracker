package iojavabrainscom.example.coronavirus_tracker.model;

public class LocationsStats {

    private String state;
    private String country;
    private String latestTotalCases;

//    public String getConfirmed() {
//        return confirmed;
//    }
//
//    public void setConfirmed(String confirmed) {
//        this.confirmed = confirmed;
//    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String county) {
        this.country = county;
    }

    public String getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(String latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    @Override
    public String toString() {
        return "LocationsStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }
}

package iojavabrainscom.example.coronavirus_tracker.Service;
import iojavabrainscom.example.coronavirus_tracker.model.LocationsStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

@Service
public class CoronaVirusDataService {

    private static URL VIRUS_DATA_TOTAL_URL;
    private static URL VIRUS_DATA_DEATHS_URL;
    private static URL VIRUS_DATA_Recovered_URL;
    private static String date = null;
    ArrayList<LocationsStats> currentStats = new ArrayList<>();

    public ArrayList<LocationsStats> getCurrentStats() {
        return currentStats;
    }

    public static String getDate() {
        return date;
    }

    static {
        try {

            VIRUS_DATA_TOTAL_URL = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
            VIRUS_DATA_DEATHS_URL = new URL( "https://data.humdata.org/hxlproxy/api/data-preview.csv?url=https%3A%2F%2Fraw.githubusercontent.com%2FCSSEGISandData%2FCOVID-19%2Fmaster%2Fcsse_covid_19_data%2Fcsse_covid_19_time_series%2Ftime_series_covid19_deaths_global.csv");
            VIRUS_DATA_Recovered_URL = new URL("https://data.humdata.org/hxlproxy/api/data-preview.csv?url=https%3A%2F%2Fraw.githubusercontent.com%2FCSSEGISandData%2FCOVID-19%2Fmaster%2Fcsse_covid_19_data%2Fcsse_covid_19_time_series%2Ftime_series_covid19_recovered_global.csv");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    @Scheduled(cron= "* * 1 * * *")
    public void getVirusData() throws IOException {
        ArrayList<LocationsStats> newStats = new ArrayList<>();
        // Get the input stream through URL Connection
        URLConnection con = VIRUS_DATA_TOTAL_URL.openConnection();
        URLConnection con2 = VIRUS_DATA_DEATHS_URL.openConnection();
        URLConnection con3 = VIRUS_DATA_Recovered_URL.openConnection();
        InputStream is1 =con.getInputStream();
        InputStream is2 = con2.getInputStream();
        InputStream is3 = con3.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is1));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
        BufferedReader br3 = new BufferedReader(new InputStreamReader(is3));
        String line = null;


        //set State and Country
        while ((line = br.readLine()) != null) {
            StringBuffer sb = new StringBuffer(line);
            if (line.startsWith(",")) {
                line = sb.insert(0, "NA").toString();
            }
            LocationsStats locationsStats = new LocationsStats();
            Reader in = new StringReader(line);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                locationsStats.setState(record.get(0));
                locationsStats.setCountry(record.get(1));
                locationsStats.setLatestTotalCases(record.get(record.size() - 1));
                newStats.add(locationsStats);

            }
            this.currentStats = newStats;
        }

        //set deaths
        int index = 0;
        while ((line = br2.readLine()) != null) {
            StringBuffer sb = new StringBuffer(line);
            if (line.startsWith(",")) {
                line = sb.insert(0, "NA").toString();
            }
            Reader in = new StringReader(line);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                currentStats.get(index).setDeaths(record.get(record.size()-1));
                index++;
            }

        }

       //recovered data not aligned with death and latest total cases
//        index = 0;
//        while ((line = br3.readLine()) != null) {
//            StringBuffer sb = new StringBuffer(line);
//            if (line.startsWith(",")) {
//                line = sb.insert(0, "NA").toString();
//            }
//            Reader in = new StringReader(line);
//            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
//            for (CSVRecord record : records) {
//                currentStats.get(index).setRecovered(record.get(record.size()-1));
//                index++;
//            }
//
//        }
        date = currentStats.get(0).getLatestTotalCases();
        for(LocationsStats stat:currentStats){
            System.out.println(stat.toString());
        }


    }
}

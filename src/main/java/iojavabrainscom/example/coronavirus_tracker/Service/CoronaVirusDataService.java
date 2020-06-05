package iojavabrainscom.example.coronavirus_tracker.Service;
import iojavabrainscom.example.coronavirus_tracker.model.LocationsStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;

@Service
public class CoronaVirusDataService {

    private static URL VIRUS_DATA_URL;
    ArrayList<LocationsStats> currentStats = new ArrayList<>();

    public ArrayList<LocationsStats> getCurrentStats() {
        return currentStats;
    }

    static {
        try {
            VIRUS_DATA_URL = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    @Scheduled(cron= "* * 1 * * *")
    public void getVirusData() throws IOException {
        ArrayList<LocationsStats> newStats = new ArrayList<>();
        // Get the input stream through URL Connection
        URLConnection con = VIRUS_DATA_URL.openConnection();
        InputStream is =con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;


        while ((line = br.readLine()) != null) {
            StringBuffer sb = new StringBuffer(line);
            if (line.startsWith(",")) {
                line = sb.insert(0, "NA").toString();
            }


            Reader in = new StringReader(line);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                LocationsStats locationsStats = new LocationsStats();
                locationsStats.setState(record.get(0));
                locationsStats.setCountry(record.get(1));
                locationsStats.setLatestTotalCases(record.get(record.size() - 1));
                System.out.println(locationsStats.toString());
                newStats.add(locationsStats);

            }
            this.currentStats = newStats;
        }


    }
}

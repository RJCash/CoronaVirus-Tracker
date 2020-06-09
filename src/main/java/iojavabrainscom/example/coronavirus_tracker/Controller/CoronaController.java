package iojavabrainscom.example.coronavirus_tracker.Controller;

import iojavabrainscom.example.coronavirus_tracker.Service.CoronaVirusDataService;
import iojavabrainscom.example.coronavirus_tracker.model.LocationsStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class CoronaController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String Home(Model model) throws IOException {
        ArrayList<LocationsStats> stats = coronaVirusDataService.getCurrentStats();

        String date = coronaVirusDataService.getDate();
        if(stats.get(0).getState().equals("Province/State")){
            stats.remove(0);
        }

        int total_sum = 0;
        for (LocationsStats stat: stats){
            total_sum += Integer.parseInt(stat.getLatestTotalCases());
        }
        model.addAttribute("locationStats", stats);
        model.addAttribute("totalSum",total_sum);
        model.addAttribute("date",date);
        return "index";
    }

    public boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

package iojavabrainscom.example.coronavirus_tracker.Controller;

import iojavabrainscom.example.coronavirus_tracker.Service.CoronaVirusDataService;
import iojavabrainscom.example.coronavirus_tracker.model.LocationsStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class CoronaController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String Home(Model model){
        ArrayList<LocationsStats> stats = coronaVirusDataService.getCurrentStats();
        stats.remove(0);
        model.addAttribute("locationStats", stats);
        return "index";
    }
}

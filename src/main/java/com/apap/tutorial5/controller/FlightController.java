package com.apap.tutorial5.controller;
import com.apap.tutorial5.model.FlightModel;
import com.apap.tutorial5.model.PilotModel;
import com.apap.tutorial5.service.FlightService;
import com.apap.tutorial5.service.PilotService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired 
	private PilotService pilotService;
	

	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		pilot.setPilotFlight(new ArrayList<FlightModel>());
		pilot.getPilotFlight().add(new FlightModel());
		model.addAttribute("pilot", pilot);
		return "addFlight";
	}
	
	@RequestMapping(value = "/flight/add", method = RequestMethod.POST)
	private String addFlightSubmit(@ModelAttribute FlightModel flight) {
		flightService.addFlight(flight);
		return "add";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"add"})
	public String addRow(@ModelAttribute PilotModel pilot, Model model) {
		pilot.getPilotFlight().add(new FlightModel());
	    	model.addAttribute("pilot", pilot);
	   	return "addFlight";
	}
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", params={"submit"}, method = RequestMethod.POST)
	private String addFlightSubmit(@ModelAttribute PilotModel pilot) {
		PilotModel pilots = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
		for (FlightModel flight:pilot.getPilotFlight()) {
			flight.setPilot(pilots);
			flightService.addFlight(flight);
		}
		return "add";
	}
	
	
    @RequestMapping(value = "/flight/delete", method = RequestMethod.POST)
    private String deleteFlight(@ModelAttribute PilotModel pilot, Model model) {
        for(FlightModel flight : pilot.getPilotFlight()) {
        	flightService.deleteFlightById(flight.getId());
        }
        return "delete";
    }
    
    @RequestMapping(value = "/flight/all-list")
	private String viewAll(Model model) {
		model.addAttribute("flightList", flightService.viewAll());
		return "view-all";
	}
    
    @RequestMapping(value = "/flight/update/{flightId}", method = RequestMethod.POST)
    private String updateFlight(@PathVariable(value = "flightId") long flightId, @ModelAttribute FlightModel flightModel) {
        flightModel.setId(flightId);
        flightService.addFlight(flightModel);

        return "update";
    }

}

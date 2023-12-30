package telran.drones.controller;

import telran.drones.api.*;
import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneMedication;
import telran.drones.dto.DroneNumberMedicationsAmount;
import telran.drones.dto.LogDto;
import telran.drones.dto.MedicationDto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.service.DronesService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DronesController {
	final DronesService dronesService;
	@PostMapping(UrlConstants.DRONES)
	DroneDto registerDrone(@RequestBody @Valid DroneDto droneDto) {
		log.debug("received: {}", droneDto);
		return dronesService.registerDrone(droneDto);		
	}
	@PostMapping(UrlConstants.LOAD_DRONE)	
	LogDto loadDrone(@RequestBody @Valid DroneMedication droneMedication) {
		LogDto res = dronesService.loadDrone(droneMedication.droneNumber(), droneMedication.medicationCode());
		log.debug("loaded logDto: {}", res);
		return res;		
	}

	@GetMapping(UrlConstants.GET_DRONES_AVAILABLE)
	List<DroneDto> availableDronesForLoading() {
		List<DroneDto> res = dronesService.availableDronesForLoading();
		log.trace("founded drones: {}", res);
		return res;
	}
	
//	- checking drone battery level for a given drone;
	//TODO	Byte getPercentage(String droneNumber);
	
//	- checking history/event log for a given drone;
	//TODO	List<LogDto> eventsByDrone(String droneNumber);
	
//	- check how many medication items have been loaded for all drones, ordered by the amount in the descending order;
	//TODO	List<DroneNumberMedicationsAmount> amountMedicationsForAllDrones();

}

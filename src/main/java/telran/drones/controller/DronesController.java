package telran.drones.controller;

import telran.drones.api.*;
import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneMedication;
import telran.drones.dto.DroneNumberMedicationsAmount;
import telran.drones.dto.LogDto;
import telran.drones.dto.MedicationDto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping(UrlConstants.GET_DRONE_BATTERY_CAPACITY + "{" + UrlConstants.DRONE_NUMBER_IN_PATH + "}")
		Byte getPercentage(@PathVariable(name=UrlConstants.DRONE_NUMBER_IN_PATH) String droneNumber) {
			Byte res = dronesService.getPercentage(droneNumber);
			log.trace("drone {} battery percentage: {}", droneNumber, res);
			return res;
		}
	
	@GetMapping(UrlConstants.GET_DRONE_LOGS + "{" + UrlConstants.DRONE_NUMBER_IN_PATH + "}")
		List<LogDto> eventsByDrone(@PathVariable(name=UrlConstants.DRONE_NUMBER_IN_PATH) String droneNumber) {
			log.debug("recieved drone number: {}", droneNumber);
			List<LogDto> res = dronesService.eventsByDrone(droneNumber);
			log.trace("logs: {}", res);
			return res;
		}
	
	@GetMapping(UrlConstants.GET_DRONE_MEDICATIONS)
	List<DroneNumberMedicationsAmount> amountMedicationsForAllDrones() {
		List<DroneNumberMedicationsAmount> res = dronesService.amountMedicationsForAllDrones();
		log.trace("drones-medications: {}", res);
		return res;
	}

}

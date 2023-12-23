package drones.drones.controller;

import telran.drones.api.*;
import telran.drones.dto.DroneDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.service.DroneService;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstants.DRONES)
@Slf4j
public class DronesController {
	final DroneService droneService;
	@PostMapping
	DroneDto registerDrone(@RequestBody @Valid DroneDto droneDto) {
		log.debug("received: {}", droneDto);
		return droneService.registerDrone(droneDto);
	}

}

package telran.drones.service;

import java.util.List;

import telran.drones.dto.*;

public interface DroneService {
	DroneDto registerDrone(DroneDto droneDto);
	LogDto loadDrone(String droneNumber, String medicationCode);
	List<MedicationDto> checkLoadMedicationsByDrone(String droneNumber);

}

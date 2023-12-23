package telran.drones.service;

import java.util.List;

import telran.drones.dto.*;
import telran.drones.entities.*;

public interface DroneService {
	DroneDto registerDrone(Drone drone);
	LogDto loadDrone(String droneNumber, String medicationCode);
	List<MedicationDto> checkLoadMedicationsByDrone(String droneNumber);

}

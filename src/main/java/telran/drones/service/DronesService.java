package telran.drones.service;

import java.util.List;

import telran.drones.dto.*;

public interface DronesService {
	DroneDto registerDrone(DroneDto droneDto);
	LogDto loadDrone(String droneNumber, String medicationCode);
	List<MedicationDto> checkLoadMedicationsByDrone(String droneNumber);
	
//	- checking available drones for loading;
	List<DroneDto> availableDronesForLoading();
//	- checking drone battery level for a given drone;
	Byte getPercentage(String droneNumber);
//	- checking history/event log for a given drone;
	List<LogDto> eventsByDrone(String droneNumber);
//	- check how many medication items have been loaded for all drones, ordered by the amount in the descending order;
	List<DroneNumberMedicationsAmount> amountMedicationsForAllDrones();

}

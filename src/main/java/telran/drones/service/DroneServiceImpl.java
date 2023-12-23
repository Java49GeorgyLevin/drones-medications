package telran.drones.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import telran.drones.dto.DroneDto;
import telran.drones.dto.LogDto;
import telran.drones.dto.MedicationDto;

@Service
@NoArgsConstructor
public class DroneServiceImpl implements DroneService {

	@Override
	public DroneDto registerDrone(DroneDto droneDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogDto loadDrone(String droneNumber, String medicationCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MedicationDto> checkLoadMedicationsByDrone(String droneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}

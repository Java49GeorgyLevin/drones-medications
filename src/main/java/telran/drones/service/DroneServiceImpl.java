package telran.drones.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.drones.api.PropertiesNames;
import telran.drones.dto.*;
import telran.drones.entities.Drone;
import telran.drones.entities.EventLog;
import telran.drones.entities.Medication;
import telran.drones.exceptions.DroneAlreadyExistException;
import telran.drones.exceptions.DroneNotFoundException;
import telran.drones.exceptions.IllegalDroneStateException;
import telran.drones.exceptions.IllegalMedicationWeightException;
import telran.drones.exceptions.LowBatteryCapacityException;
import telran.drones.exceptions.MedicationNotFoundException;
import telran.drones.repo.*;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
	final DroneRepo droneRepo;
	final MedicationRepo medicationRepo;
	final LogRepo logRepo;
	final ModelMapper modelMapper;
	@Value("${" + PropertiesNames.PERCENTAGE_THRESHOLD + ":25}")
	byte percentageThreshold;
	
	@Override
	@Transactional
	public DroneDto registerDrone(DroneDto droneDto) {		
		if(droneRepo.existsById(droneDto.getSerialNumber())) {
			throw new DroneAlreadyExistException();			
		}
		Drone drone = modelMapper.map(droneDto, Drone.class);
		droneRepo.save(drone);
		return droneDto;
	}

	@Override
	@Transactional(readOnly = false)
	public LogDto loadDrone(String droneNumber, String medicationCode) {
		Drone drone = droneRepo.findById(droneNumber)
				.orElseThrow(() -> new DroneNotFoundException());
	
		Medication medication = medicationRepo.findById(medicationCode)
				.orElseThrow(() -> new MedicationNotFoundException());
		
		if(drone.getState() != DroneState.IDLE) {
			throw new IllegalDroneStateException();
		}
		if(drone.getBatteryPercentage() < percentageThreshold) {
			throw new LowBatteryCapacityException();
		}
		if(drone.getWeightLimit() < medication.getWeight()) {
			throw new IllegalMedicationWeightException(); 
		}
		drone.setDroneState(DroneState.LOADED);
		EventLog log = new EventLog(drone, medication, LocalDateTime.now());
		logRepo.save(log);
		LogDto logDto = modelMapper.map(log, LogDto.class);
		return logDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MedicationDto> checkLoadMedicationsByDrone(String droneNumber) {
		Drone drone = droneRepo.findById(droneNumber)
				.orElseThrow(() -> new DroneNotFoundException());
		EventLog log = logRepo.findByDrone(drone);
		Medication medication = log.getMedication();
		MedicationDto mDto = modelMapper.map(medication, MedicationDto.class);
		return (List<MedicationDto>) mDto;				
	}

}

package telran.drones.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.api.PropertiesNames;
import telran.drones.dto.*;
import telran.drones.entities.*;
import telran.drones.exceptions.*;
import telran.drones.repo.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DronesServiceImpl implements DronesService {
	final DroneRepo droneRepo;
	final MedicationRepo medicationRepo;
	final LogRepo logRepo;
	final ModelMapper modelMapper;
	final Map<DroneState, DroneState> movesMap;
	@Value("${" + PropertiesNames.PERCENTAGE_THRESHOLD + ":25}")
	byte percentageThreshold;
	@Value("${" + PropertiesNames.PERIODIC_UNIT_MILLIS + ":100}")
	long millisPerTimeUnit;
	@Value("${" + PropertiesNames.CAPACITY_DELTA_TIME_UNIT + ":2}")
	byte capacityDeltaPerTimeUnit;
	
	
	@Override
	@Transactional
	public DroneDto registerDrone(DroneDto droneDto) {		
		if(droneRepo.existsById(droneDto.getNumber())) {
			throw new DroneAlreadyExistException();			
		}
		Drone drone = modelMapper.map(droneDto, Drone.class);
		log.debug("mapped drone is: {}", drone);
		drone.setState(DroneState.IDLE);
		droneRepo.save(drone);
		return droneDto;
	}

	@Override
	@Transactional(readOnly = false)
	public LogDto loadDrone(String droneNumber, String medicationCode) {
		log.debug("received droneNumber={}, medicationCode={}", droneNumber, medicationCode);
		log.debug("percentage threshold is {}", percentageThreshold);
		Drone drone = droneRepo.findById(droneNumber)
				.orElseThrow(() -> new DroneNotFoundException());
		log.debug("found drone: {}", drone);
	
		Medication medication = medicationRepo.findById(medicationCode)
				.orElseThrow(() -> new MedicationNotFoundException());
		log.debug("found medication: {}", medicationCode);
		
		if(drone.getState() != DroneState.IDLE) {
			throw new IllegalDroneStateException();
		}
		byte bCapacity = drone.getBatteryCapacity();
		if(bCapacity < percentageThreshold) {
			throw new LowBatteryCapacityException();
		}
		if(drone.getWeightLimit() < medication.getWeight()) {
			throw new IllegalMedicationWeightException(); 
		}
		drone.setState(DroneState.LOADING);
		EventLog eventLog = new EventLog(drone, medication, LocalDateTime.now(), drone.getState(), bCapacity );
		logRepo.save(eventLog);
		LogDto logDto = eventLog.build(); 
		log.debug("saved log: {}", logDto);
		return logDto;
	}
	@Override
	public List<MedicationDto> checkLoadMedicationsByDrone(String droneNumber) {
		log.debug("received drone number: {}", droneNumber);
		if(!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		List<EventLog> logs = logRepo.findByDroneNumberAndDroneState(droneNumber, DroneState.LOADING);
		log.trace("found following logs: {}", logs.stream().map(EventLog::build).toList());
		List<MedicationDto> medicationsDto = logs.stream().map(l ->
				modelMapper.map(l.getMedication(), MedicationDto.class)).toList();
		return medicationsDto;				
	}

	@Override
	public List<DroneDto> availableDronesForLoading() {
		List<Drone> drones = droneRepo.getAvailableForLoading(percentageThreshold);
		drones.forEach(d -> System.out.printf("%s\n", d.getNumber()));
		log.trace("available drones: {}", drones);
		List<DroneDto> dronesDto = drones.stream().map(d -> modelMapper.map(d, DroneDto.class)).toList();
		return dronesDto;
	}

	@Override
	public Byte getPercentage(String droneNumber) {
		Drone drone = droneRepo.findById(droneNumber)
				.orElseThrow(() -> new DroneNotFoundException());
		return drone.getBatteryCapacity();
	}

	@Override
	public List<LogDto> eventsByDrone(String droneNumber) {
		if(droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		};
		List<EventLog> logs = logRepo.findByDroneNumberOrderByTimestampDesc(droneNumber);
		log.trace("found logs: {}", logs);
		List<LogDto> logsDto = logs.stream().map(EventLog::build).toList();
		return logsDto;
	}

	@Override
	public List<DroneNumberMedicationsAmount> amountMedicationsForAllDrones() {
		List<DroneNumberMedicationsAmount> dronesMedications = logRepo.getAmountMedicationsForAllDrones();
		log.debug("drone-medications: {}", dronesMedications);
		return dronesMedications;
	}
	@PostConstruct
	void periodicTask() {
		Thread thread = new Thread(() -> {
			try {
				while(true) {
					Thread.sleep(millisPerTimeUnit);
//check analog:					List<Drone> drones = droneRepo.findAll();
//check analog:					drones.forEach(d -> chargingDischarging(d));					
 
							droneRepo.findAllBy()
							.forEach(d -> {
								log.debug("drone {} before charging capacity: {}, state: {}", 
										d.getNumber(), d.getBatteryCapacity(), d.getState());
								chargingDischarging(d);
								log.debug("drone {} after charging capacity: {}, state: {}", 
										d.getNumber(), d.getBatteryCapacity(), d.getState());
							});


				}
				
			} catch (InterruptedException e) {
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
	@Transactional
	private void chargingDischarging(Drone drone) {
		DroneState dState = drone.getState();
		byte bCapacity = drone.getBatteryCapacity();
		if(dState != DroneState.IDLE) {
			 drone.setState(movesMap.get(dState));
			 drone.setBatteryCapacity((byte) (bCapacity - capacityDeltaPerTimeUnit));
			 newLog(drone);
		}
		if(dState == DroneState.IDLE && bCapacity < 100) {
			drone.setBatteryCapacity((byte) (bCapacity + capacityDeltaPerTimeUnit));
		}		

	}
	
	private void newLog(Drone drone) {
		String droneNumber = drone.getNumber();
		EventLog log = logRepo.findFirst1ByDroneNumberOrderByTimestampDesc(droneNumber);
		Medication medication = log.getMedication();
		EventLog newLog = new EventLog(drone, medication, LocalDateTime.now(), drone.getState(), drone.getBatteryCapacity());
		logRepo.save(newLog);
		
	}
}

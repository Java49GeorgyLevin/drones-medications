package telran.drones;
import telran.drones.api.*;
import telran.drones.dto.DroneNumberMedicationsAmount;
import telran.drones.dto.DroneState;
import telran.drones.entities.Drone;
import telran.drones.entities.EventLog;
import telran.drones.exceptions.DroneNotFoundException;
import telran.drones.exceptions.IllegalDroneStateException;
import telran.drones.exceptions.MedicationNotFoundException;
import telran.drones.repo.*;
import telran.drones.service.DronesService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest(properties = {PropertiesNames.PERIODIC_UNIT_MICROS  + "=1000000"})
@Sql(scripts = "classpath:test_data.sql")
//('Drone-1', 'Middleweight', 300, 100, 'IDLE'),
//('Drone-2', 'Middleweight', 300, 20, 'IDLE'),
//('Drone-3', 'Middleweight', 300, 100, 'LOADING'),
//('Drone-4', 'Middleweight', 500, 80, 'IDLE');
//('MED_1', 'Medication-1', 200),
//('MED_2', 'Medication-2', 350)

@Slf4j
class DronesServiceStaticTest {
	private static final String DRONE1 = "Drone-1";
	private static final String DRONE2 = "Drone-2";
	private static final String MED1 = "MED_1";
	private static final String DRONE3 = "Drone-3";
	private static final String SERVICE_TEST = "Service: ";
	private static final String DRONE4 = "Drone-4";
	private static final String MED2 = "MED_2";
	@Autowired
 DronesService dronesService;
	@Autowired
	DroneRepo droneRepo;
	@Autowired
	LogRepo logRepo;
	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_NORMAL)
	void loadDroneNormal() {
		dronesService.loadDrone(DRONE1, MED1);
		List<EventLog> logs = logRepo.findAll();
		assertEquals(1, logs.size());
		EventLog loadingLog = logs.get(0);
		Drone drone = loadingLog.getDrone();
		assertEquals(DRONE1, drone.getNumber());
		assertEquals(DroneState.LOADING, drone.getState());
		assertEquals(MED1, loadingLog.getMedication().getCode());
	}
	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_NOT_MATCHING_STATE)
	void loadDroneWrongState() {
		assertThrowsExactly(IllegalDroneStateException.class,
				() -> dronesService.loadDrone(DRONE3, MED1));
	}
	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_MEDICATION_NOT_FOUND)
	void loadDroneMedicationNotFound() {
		assertThrowsExactly(MedicationNotFoundException.class,
				() -> dronesService.loadDrone(DRONE1, "KUKU"));
	}
	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_NOT_FOUND)
	void loadDroneNotFound() {
		assertThrowsExactly(DroneNotFoundException.class,
				() -> dronesService.loadDrone("Drone-NF", MED1));
	}
	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.GET_ALL_AVAILABLE_DRONES_FOR_LOADING)
	void availableDronesForLoadingTest() {
		dronesService.availableDronesForLoading();
		
	}
	

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.CHECK_MEDICATIONS_BY_EACH_DRONE)
	@Sql(scripts = {"classpath:test_data.sql"})
	@Transactional(readOnly = false)
	void amountMedicationsForAllDronesTest() {
		
		dronesService.loadDrone(DRONE1, MED1);
		dronesService.loadDrone(DRONE4, MED2);
		Drone drone = droneRepo.getReferenceById(DRONE4);
		drone.setState(DroneState.IDLE);
		log.debug("drone4: {}, state:{}", drone.getNumber(), drone.getState());
		dronesService.loadDrone(DRONE4, MED1);
		drone.setState(DroneState.IDLE);
		dronesService.loadDrone(DRONE4, MED2);
		List<DroneNumberMedicationsAmount> listRes = dronesService.amountMedicationsForAllDrones();
		Map<String, Long> mapRes = listRes.stream()
				.collect(Collectors.toMap(lr -> lr.getNumber(), lr -> lr.getAmount()));
		assertEquals(4, mapRes.size());
		Map<String, Long> mapExpection = Map.of("Drone-4", 3L, "Drone-1", 1L, "Drone-2", 0L, "Drone-3", 0L);
		assertEquals(mapExpection, mapRes);	
		
	}

}

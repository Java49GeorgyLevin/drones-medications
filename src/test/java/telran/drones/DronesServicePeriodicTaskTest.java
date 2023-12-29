package telran.drones;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.drones.api.PropertiesNames;
import telran.drones.dto.*;
import telran.drones.service.DronesService;

@SpringBootTest(properties = { PropertiesNames.PERIODIC_UNIT_MICROS + "=10" })
@Sql(scripts = { "test_data_normal_idle.sql" })
//('Drone-1', 'Middleweight', 300, 100, 'IDLE'),
// ('Drone-2', 'Middleweight', 300, 100, 'IDLE'),
// ('Drone-3', 'Middleweight', 300, 100, 'IDLE');
// ('MED_1', 'Medication-1', 200),
// ('MED_2', 'Medication-2', 200)
class DronesServicePeriodicTaskTest {
	private static final String DRONE1 = "Drone-1";
	private static final String MED1 = "MED_1";
	@Autowired
	DronesService dronesService;

	@Test
	void test() throws InterruptedException {
		// At this step there should be three available drones
		availableDronesTest(3);
		dronesService.loadDrone(DRONE1, MED1);
		Thread.sleep(500);
		// At this step there should be two available drones
		availableDronesTest(2);
		Thread.sleep(510);
		// At this step there should be
		// three available drones
		// battery capacity of Drone-1 78%
		// number of logs 12
		availableBatteryLogsTest(3, 78, 12);
		Thread.sleep(1100);
		// At this step there should be
		// three available drones
		// battery capacity of Drone-1 100%
		// number of logs 12 + 11 = 23
		availableBatteryLogsTest(3, 100, 23);

	}

	private void availableBatteryLogsTest(int nAvailableDrones, int batteryCapacity, int nLogs) {
		availableDronesTest(3);
		batteryCapacityTest(78);
		logsNumberTest(12);
	}

	private void logsNumberTest(int nLogs) {
		//TODO
//		List<LogDto> logs = dronesService.checkLogs(DRONE1);
//		assertEquals(nLogs, logs.size());

	}

	private void batteryCapacityTest(int capacityExpected) {
		//TODO
//		int capacityActual = dronesService.checkBatteryLevel(DRONE1);
//		assertEquals(capacityExpected, capacityActual);

	}

	private void availableDronesTest(int nDrones) {
		//TODO
//		List<DroneDto> availableDrones = dronesService.checkAvailableDrones();
//		assertEquals(nDrones, availableDrones.size());
	}

}

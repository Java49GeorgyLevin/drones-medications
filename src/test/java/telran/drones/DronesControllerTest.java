package telran.drones;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.drones.dto.*;
import telran.drones.exceptions.DroneAlreadyExistException;
import telran.drones.exceptions.DroneNotFoundException;
import telran.drones.exceptions.LowBatteryCapacityException;
import telran.drones.exceptions.MedicationNotFoundException;
import telran.drones.service.DronesService;
import telran.exceptions.GlobalExceptionsHandler;

import static telran.drones.api.ConstraintConstants.*;
import static telran.drones.api.ServiceExceptionMessages.*;
import static telran.drones.TestDisplayNames.*;
import static telran.drones.api.UrlConstants.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@WebMvcTest
class DronesControllerTest {
	private static final String HOST = "http://localhost:8080/";
	String DRONE_NUMBER = "DRON-1";
	String MEDICATION_CODE = "MED_1";
	DroneDto droneDto1 = new DroneDto("D-1", DroneModel.Middleweight, 300, (byte) 100, DroneState.IDLE);
	DroneDto droneDto2 = new DroneDto("D-2", DroneModel.Cruiserweight, 350, (byte) 80, DroneState.IDLE);
	MedicationDto medicationDto1 = new MedicationDto("CODE_1", "Medication-1", 200);
	MedicationDto medicationDto2 = new MedicationDto("CODE_2", "Medication-2", 300);
	LogDto logDto = new LogDto(LocalDateTime.now(), DRONE_NUMBER, DroneState.LOADING, 100, MEDICATION_CODE);
	DroneMedication droneMedication = new DroneMedication(DRONE_NUMBER, MEDICATION_CODE);

	DroneDto droneDtoWrongFields = new DroneDto(new String(new char[10000]), DroneModel.Middleweight, 600, (byte) 101,
			DroneState.IDLE);
	DroneDto droneDtoMissingFields = new DroneDto(null, null, null, null, null);
	String[] errorMessagesWrongFields = { DRONE_NUMBER_WRONG_LENGTH, MAX_PERCENTAGE_VIOLATION, MAX_WEIGHT_VIOLATION,

	};
	String[] errorMessagesMissingFields = { MISSING_BATTERY_CAPACITY, MISSING_DRONE_NUMBER, MISSING_MODEL,
			MISSING_STATE, MISSING_WEIGHT_LIMIT,
	};
	final String CONTROLLER = "Controller: ";
	
	@Autowired
	MockMvc mockMvc;
	@MockBean
	DronesService dronesService;
	@Autowired
	ObjectMapper mapper;

@Test
@DisplayName("Controller:" + REGISTER_DRONE_NORMAL)
	void testDroneRegisterNormal() throws Exception{
		when(dronesService.registerDrone(droneDto1)).thenReturn(droneDto1);
		String droneJSON = mapper.writeValueAsString(droneDto1);
		String response = mockMvc.perform(post(HOST + DRONES ).contentType(MediaType.APPLICATION_JSON)
				.content(droneJSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(droneJSON, response);
		
	}

	@Test
	@DisplayName("Controller:" + REGISTER_DRONE_MISSING_FIELDS)
	void testDronRegisterMissingFields() throws Exception {
		String droneJSON = mapper.writeValueAsString(droneDtoMissingFields);
		String response = mockMvc
				.perform(post(HOST + DRONES).contentType(MediaType.APPLICATION_JSON).content(droneJSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertErrorMessages(response, errorMessagesMissingFields);
	}

	private void assertErrorMessages(String response, String[] expectedMessages) {
		String [] actualMessages = response.split(GlobalExceptionsHandler.ERROR_MESSAGES_DELIMITER);
		Arrays.sort(actualMessages);
		Arrays.sort(expectedMessages);
		assertArrayEquals(expectedMessages, actualMessages);
	}

	@Test
	@DisplayName("Controller:" + REGISTER_DRONE_VALIDATION_VIOLATION)
	void testDronRegisterWrongFields() throws Exception {
		String droneJSON = mapper.writeValueAsString(droneDtoWrongFields);
		String response = mockMvc
				.perform(post(HOST + DRONES).contentType(MediaType.APPLICATION_JSON).content(droneJSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertErrorMessages(response, errorMessagesWrongFields);
	}

@Test
@DisplayName("Controller:" + REGISTER_DRONE_ALREADY_EXISTS)
void testDroneRegisterAlreadyExists() throws Exception{
	when(dronesService.registerDrone(droneDto1)).thenThrow(new DroneAlreadyExistException());
	String droneJSON = mapper.writeValueAsString(droneDto1);
	String response = mockMvc.perform(post(HOST + DRONES ).contentType(MediaType.APPLICATION_JSON)
			.content(droneJSON)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
	assertEquals(DRONE_ALREADY_EXISTS, response);
	
}
	@Test
	@DisplayName(CONTROLLER + LOAD_DRONE_NORMAL) 
	void loadDroneTest() throws Exception {
		when(dronesService.loadDrone(DRONE_NUMBER, MEDICATION_CODE)).thenReturn(logDto);		
		String droneMedicationJSON = mapper.writeValueAsString(droneMedication);
		String logDtoJSON = mapper.writeValueAsString(logDto);		
		String response = mockMvc.perform(post(HOST + LOAD_DRONE).contentType(MediaType.APPLICATION_JSON)
				.content(droneMedicationJSON)).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();		
		assertEquals(logDtoJSON, response);				
	}
	@Test
	@DisplayName(CONTROLLER + LOAD_DRONE_NOT_FOUND)
	void loadDroneNotFound() throws Exception {
		RuntimeException serviceException = new DroneNotFoundException();
		when(dronesService.loadDrone(DRONE_NUMBER, MEDICATION_CODE)).thenThrow(serviceException);
		String droneMedicationJSON = mapper.writeValueAsString(droneMedication);
		String response = mockMvc.perform(post(HOST+LOAD_DRONE)
				.contentType(MediaType.APPLICATION_JSON).content(droneMedicationJSON))
				.andExpect(status().is(404))
			.andReturn().getResponse().getContentAsString();
		assertEquals(serviceException, response);
	}
	
	
	@Test
	@DisplayName(CONTROLLER + LOAD_DRONE_MEDICATION_NOT_FOUND)
	void loadDroneMedicationNotFound() throws Exception {
		RuntimeException serviceException = new MedicationNotFoundException();		
		when(dronesService.loadDrone(DRONE_NUMBER, MEDICATION_CODE)).thenThrow(serviceException);
		String droneMedicationDto = mapper.writeValueAsString(droneMedication);
		String response = mockMvc.perform(post(HOST + LOAD_DRONE)
				.contentType(MediaType.APPLICATION_JSON).content(droneMedicationDto))
				.andExpect(status().is(404))
			.andReturn().getResponse().getContentAsString();
		assertEquals(serviceException, response);		
	}
	
	@Test
	@DisplayName(CONTROLLER + LOAD_DRONE_LOW_BATTERY_CAPCITY)
	void loadDroneLowBatteryCapacity() throws Exception {
		String droneMedicationDto = mapper.writeValueAsString(droneMedication);
		RuntimeException serviceException = new LowBatteryCapacityException();
		when(dronesService.loadDrone(DRONE_NUMBER, MEDICATION_CODE)).thenThrow(serviceException);
		String response = mockMvc.perform(post(HOST + LOAD_DRONE)
				.contentType(MediaType.APPLICATION_JSON).content(droneMedicationDto))
				.andExpect(status().is(400))
				.andReturn().getResponse().getContentAsString();
		assertEquals(serviceException, response);
		
	}
	
	

	@Test
	@DisplayName(CONTROLLER + CHECK_MEDICATIONS_BY_DRONE)
	void checkLoadMedicationsByDroneTest() {
		
	}

}

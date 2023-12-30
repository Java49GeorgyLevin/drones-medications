package telran.drones.configuration;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import telran.drones.dto.DroneState;

@Configuration
@EnableScheduling
public class DronesConfiguration {
	@Bean
	ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
		.setFieldMatchingEnabled(true)
		.setFieldAccessLevel(AccessLevel.PRIVATE);
		return modelMapper;
	}
	@Bean
	Map<DroneState, DroneState> getMovesMap() {
		Map<DroneState, DroneState> res = new HashMap<>();
		res.put(DroneState.LOADING, DroneState.LOADED);
		res.put(DroneState.LOADED, DroneState.DELIVERING);
		res.put(DroneState.DELIVERING, DroneState.DELIVERING1);
		res.put(DroneState.DELIVERING1, DroneState.DELIVERING2);
		res.put(DroneState.DELIVERING2, DroneState.DELIVERING3);
		res.put(DroneState.DELIVERING3, DroneState.DELIVERED);
		res.put(DroneState.DELIVERED, DroneState.RETURNING);
		res.put(DroneState.RETURNING, DroneState.RETURNING1);
		res.put(DroneState.RETURNING1, DroneState.RETURNING2);
		res.put(DroneState.RETURNING2, DroneState.RETURNING3);
		res.put(DroneState.RETURNING3, DroneState.IDLE);
		return res;
	}
}

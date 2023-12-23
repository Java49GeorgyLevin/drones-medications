package telran.drones.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import static telran.drones.api.ConstraintConstants.*;

@AllArgsConstructor
@Data
public class DroneDto {
	@Length(max = MAX_DRONE_NUMBER_SIZE)
	@NotEmpty(message = MISSING_DRONE_NUMBER)
	String serialNumber;
	@NotNull(message = MISSING_MODEL)
	DroneModel model;
	@NotNull(message = MISSING_WEIGHT_LIMIT)
	@Positive
	@Max(value = MAX_WEIGHT, message = MAX_WEIGHT_VIOLATION)
	int weightLimit;
	@Positive
	@Max(value = 100, message = MAX_PERCENTAGE_VIOLATION)
	@NotNull
	byte batteryPercentage;
	@NotNull(message = MISSING_STATE)
	DroneState state;

}

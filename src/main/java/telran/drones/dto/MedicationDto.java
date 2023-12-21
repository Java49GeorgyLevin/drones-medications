package telran.drones.dto;

import jakarta.validation.constraints.*;

public record MedicationDto(@Pattern(regexp = "[\\w\\_\\-]+") String code, 
		String name, 
		@NotNull @Positive int weight) {

}

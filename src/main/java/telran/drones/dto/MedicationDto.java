package telran.drones.dto;

import jakarta.validation.constraints.*;

public record MedicationDto(@NotNull @Positive int id, String name, @NotNull @Positive int weight, 
		@Pattern(regexp = "[\\w\\_\\-]+") String code) {

}

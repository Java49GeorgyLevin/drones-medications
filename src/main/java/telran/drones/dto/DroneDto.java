package telran.drones.dto;

import jakarta.validation.constraints.*;

public record DroneDto(@NotNull @Positive int id, @Pattern(regexp = "[\\w\\-]{1-100}") String serialNumber,
		@NotNull DroneModel model,
		@NotNull @Positive int weightLimit, @Positive float prsintage, @NotNull DroneState state) {

}

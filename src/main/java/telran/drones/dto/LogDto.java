package telran.drones.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record LogDto(LocalDateTime timestamp, String droneNumber, DroneState droneState, int batteryPercentage,
		String medicationCode) {
	@Override
	public String toString() {
		return String.format("%s: drone: %s, state: %s, battery capacity: %d, medication: %s",
				timestamp.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")), droneNumber, droneState,
				batteryPercentage, medicationCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(batteryPercentage, droneNumber, medicationCode, droneState);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogDto other = (LogDto) obj;
		return batteryPercentage == other.batteryPercentage && Objects.equals(droneNumber, other.droneNumber)
				&& Objects.equals(medicationCode, other.medicationCode) && droneState == other.droneState;
	}

}

package telran.drones.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.drones.dto.*;


@Entity
@Table(name="drones")
@NoArgsConstructor
@Getter
public class Drone {
	@Id
	@Column(nullable = false)
	String serialNumber;
	@Column(nullable = false)
	DroneModel model;
	@Column(nullable = false)
	int weightLimit;
	@Column(nullable = false)
	float persintage;
	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	DroneState state;
	public Drone(DroneDto droneDto) {
		serialNumber = droneDto.serialNumber();
		model = droneDto.model();
		weightLimit = droneDto.weightLimit();
		persintage = droneDto.prsintage();
		state = droneDto.state();
	}
	
public DroneDto build() {
	return new DroneDto(serialNumber, model, weightLimit, persintage, state);
}

@Override
public String toString() {
	return "Drone [serialNumber=" + serialNumber + ", model=" + model + ", weightLimit=" + weightLimit
			+ ", persintage=" + persintage + ", state=" + state + "]";
}

}

package telran.drones.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.drones.dto.*;

@Entity
@Table(name = "drones")
@NoArgsConstructor
@Getter
public class Drone {
	@Id
	@Column(length = 100)
	String number;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false,updatable = false)
	DroneModel model;
	@Column(nullable = false,updatable = false, name="weight_limit")
	int weightLimit;
	@Column(nullable = false,updatable = true, name="battery_capacity")
	@Setter
	byte batteryCapacity;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false,updatable = true)
	@Setter
	DroneState state;
	
}


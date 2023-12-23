package telran.drones.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.drones.dto.*;

import static telran.drones.api.ConstraintConstants.*;
@Entity
@Table(name="drones")
@NoArgsConstructor
@Getter
public class Drone {
	@Id
	@Column(length = MAX_DRONE_NUMBER_SIZE)
	String serialNumber;
	@Column(nullable = false, updatable = false)
	DroneModel model;
	@Column(nullable = false, updatable = false, name="weight_limit")
	int weightLimit;
	@Column(nullable = false, updatable = false, name="battery_persintage")
	byte batteryPercentage;
	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	DroneState state;
	
	public void setBatteryPercentage(byte pers) {
		batteryPercentage = pers;		
	}
	
	public void setDroneState(DroneState state) {
		this.state = state;
	}


}

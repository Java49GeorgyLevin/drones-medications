package telran.drones.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.drones.dto.MedicationDto;

@Entity
@Table(name="medications")
@NoArgsConstructor
@Getter
public class Medication {
	@Id
	int id;
	@Column(nullable = false)
	String name;
	@Column(nullable = false)
	int weight;
	@Column(nullable = false)
	String code;
	public Medication(MedicationDto medicationDto) {
		id = medicationDto.id();
		name = medicationDto.name();
		weight = medicationDto.weight();
		code = medicationDto.code();
	}


	public MedicationDto build() {
	
	return new MedicationDto(id, name, weight, code);	
	}


	@Override
	public String toString() {
		return "Medication [id=" + id + ", name=" + name + ", weight=" + weight + ", code=" + code + "]";
	}
	
	
}
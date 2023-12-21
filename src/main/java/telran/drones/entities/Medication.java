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
	String code;	
	@Column(nullable = false)
	String name;
	@Column(nullable = false)
	int weight;

	public Medication(MedicationDto medicationDto) {
		code = medicationDto.code();		
		name = medicationDto.name();
		weight = medicationDto.weight();

	}


	public MedicationDto build() {
	
	return new MedicationDto(code, name, weight);	
	}


	@Override
	public String toString() {
		return "Medication [code=" + code + ", name=" + name + ", weight=" + weight + "]";
	}
	
	
}
package telran.drones.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.dto.*;
import telran.drones.entities.*;

public interface LogRepo extends JpaRepository<EventLog, Long> {

	EventLog findByDrone(Drone drone);

	List<EventLog> findByDroneNumberAndDroneState(String droneNumber, DroneState state);

	@Query("SELECT d.number as number, count(el.drone) as amount FROM EventLog el "
			+ "right join el.drone d "
			+ "where el.state='LOADING' or el.drone is null "
			+ "group by d.number order by count(el.drone) desc")
	List<DroneNumberMedicationsAmount> getAmountMedicationsForAllDrones();

	List<EventLog> findByDroneNumberOrderByTimestampDesc(String droneNumber);

	EventLog findFirst1ByDroneNumberOrderByTimestampDesc(String droneNumber);

	


}

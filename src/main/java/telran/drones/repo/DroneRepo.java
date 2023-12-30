package telran.drones.repo;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.entities.*;

public interface DroneRepo extends JpaRepository<Drone, String> {
	
@Query("select d from Drone d where d.state='IDLE' and d.batteryCapacity > :percentageThreshold")
	List<Drone> getAvailableForLoading(byte percentageThreshold);

Stream<Drone> findAllBy();

}

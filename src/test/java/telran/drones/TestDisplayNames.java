package telran.drones;

public interface TestDisplayNames {
String REGISTER_DRONE_NORMAL = "Registering drone normal flow";
String REGISTER_DRONE_VALIDATION_VIOLATION = "Drone JSON with  wrong fields";
String REGISTER_DRONE_MISSING_FIELDS = "Drone JSON with missing fields";
String REGISTER_DRONE_ALREADY_EXISTS = "Registering Drone with existing number";
String LOAD_DRONE_NORMAL = "Loading Drone normal flow";
String LOAD_DRONE_NOT_FOUND = "Loading Drone Not Found";
String LOAD_DRONE_MEDICATION_NOT_FOUND = "Loading Drone Medication Not Found";
String LOAD_DRONE_LOW_BATTERY_CAPCITY = "Loading Drone Low Battery Capacity";
String LOAD_DRONE_NOT_MATCHING_WEIGHT = "Loading Drone Not Matching Weight";
String LOAD_DRONE_NOT_MATCHING_STATE = "Loading Drone State not IDLE";

String CHECK_MED_ITEMS_NORMAL = "checking Medication Items Normal Flow";
String CHECK_MED_ITEMS_DRONE_NOT_FOUND = "checking Medication Items, Drone Not Found";

String GET_ALL_AVAILABLE_DRONES_FOR_LOADING = "Checking available Drones for loading";
String CHECK_MEDICATIONS_BY_EACH_DRONE = "Check how many medication items have been loaded for all drones";
String CHECK_MEDICATIONS_BY_DRONE = "Check how many medication items have been loaded for current drone";
}

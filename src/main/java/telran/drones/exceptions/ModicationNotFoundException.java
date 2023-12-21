package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;
import telran.exceptions.NotFoundException;

@SuppressWarnings("serial")
public class ModicationNotFoundException extends NotFoundException {

	public ModicationNotFoundException() {
		super(ServiceExceptionMessages.MEDICATION_NOT_FOUND);
	}
	
}

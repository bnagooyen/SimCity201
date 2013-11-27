package simcity.interfaces;

import java.util.List;

import simcity.PersonAgent;



public interface Car {

	public abstract void msgGoToDestination(String location, Person person);
	public abstract void msgAtDestination();

}

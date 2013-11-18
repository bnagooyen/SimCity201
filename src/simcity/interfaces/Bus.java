package simcity.interfaces;

import java.util.List;

import simcity.PersonAgent;



public interface Bus {

	public abstract void msgAtStop(String stop);
	public abstract void msgHereArePassengers(List<PersonAgent> people);
	public abstract void msgGettingOn(PersonAgent p, String destination);

}

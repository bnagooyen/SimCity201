package simcity.interfaces;

import simcity.DRestaurant.DOrder;
import simcity.DRestaurant.DProducerConsumerMonitor;


public interface DCook extends Cook{

	void setMonitor(DProducerConsumerMonitor theMonitor);

	void msgHereIsAnOrder(DOrder o);

	//public abstract void msgShouldIPayThisBill(double amt, DMarket ma);
}

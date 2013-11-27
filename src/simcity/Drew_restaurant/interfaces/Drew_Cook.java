package simcity.Drew_restaurant.interfaces;


//import restaurant.WaiterAgent;
import simcity.Drew_restaurant.Drew_CookRole.Order;
import simcity.Drew_restaurant.ProducerConsumerMonitor;
import simcity.Drew_restaurant.gui.*;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Drew_Cook {

	public abstract void hereIsOrder(Drew_Waiter w, String choice, int table);
	
	public abstract void TimerDone(Order O);
	
	public abstract void remainingStock(String type, int quantityAvailable, int quantityOrdered, String name);
	
	public abstract void deliver(String type, int quantity, boolean fullOrder);
	
	public abstract void msgAtDest();
	
	public abstract CookGui getGui();

	public abstract void setMonitor(ProducerConsumerMonitor theMonitor);
	
	public abstract void msgGoHome(double pay);

}
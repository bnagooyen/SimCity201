package simcity.test.mock;


import simcity.PersonAgent;
import simcity.BRestaurant.*;
import simcity.BRestaurant.gui.BCustomerGui;
import simcity.interfaces.BCashier;
import simcity.interfaces.BCustomer;
import simcity.interfaces.BWaiter;
import simcity.mockrole.MockRole;
/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class BMockCustomer extends MockRole implements BCustomer{

	BCheck mycheck;
       
         
        public BCashierRole cashier;
        public EventLog log=new EventLog();

        public BMockCustomer(String name) {
                super(name);

        }

        public  void msgHereisYourCheck(BCheck check){
        	mycheck=check;
        	log.add(new LoggedEvent("Received msgHereisYourCheck from the Cashier!"));
        	
        	
        }
    	public  void setWaiter(BWaiter waiter){}
    	
    	public  void gotHungry(){}
    	public void setToPause(){}
    	public void msgSitAtTable(int tablenumber, BMenu menu, BWaiter waiter){}
    	public  void msgAnimationFinishedGoToSeat(){}
    	public  void msgAnimationFinishedLeaveRestaurant(){}
    	public  void msgWhatWouldYouLike(BWaiter waiter){}
    	public  void msgHereisYourOrder(String choice){}
    	
    	public  void msgdecidedOrder(){}
    	public  void msgReorder(){}
    	public  void setCashier(BCashier cashier){}
    	public  int getHungerLevel(){return 0;}
    	public  void setHungerLevel(int hungerLevel){}
    	
    	public  void setGui(BCustomerGui g){}
    	public BCustomerGui getGui(){return null;}
        

}
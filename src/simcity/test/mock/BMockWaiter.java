package simcity.test.mock;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.BRestaurant.*;
import simcity.BRestaurant.BMarketRole.cookState;
import simcity.BRestaurant.gui.BCustomerGui;
import simcity.interfaces.*;
/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class BMockWaiter extends Mock implements BWaiter{

	BCheck mycheck;
       
         
        public BCashier cashier;
        public EventLog log=new EventLog();
        private Map<String,BFood> marketStock=new HashMap <String,BFood>();
   	 private List<BFood> orderFromCook=new ArrayList<BFood>();
   	 private List<BCheck> myChecks=new ArrayList<BCheck>();
   	 private String name;
   	 public BCook cook;
   	 public cookState state; 
   	 private BCheck marketCheck;
   	 
   	 private int totalMoney=0;

        public BMockWaiter(String name) {
                super(name);
                this.name = name;
        		
        }

        public void setCashier(BCashier cashier){
        	this.cashier=cashier;
        }
    	
        public  void msgSitCustomerAtTable(BCustomer customer, int tableNum){}
    	public  void msgIWantFood(BCustomer cust){}
    	public  void msgBreakAllowed(){}
    	public  void msgBreakNotAllowed(){}
    	public  void msgLeavingTable(BCustomer cust){}
    	public  void msgAtTable(){}
    	public  void msgHereismyChoice(BCustomer customer, String choice){}
    	public  void msgOrderisReady(int tablenumber, String choice){}
    	public  void msgIWantCheck(BCustomer customer){}
    	public void msgCustomerNoMoney(){}
    	public  void msgReadytoOrder(BCustomer customer){}
    	
    	public  void msgOutOfThatFood(int tablenumber){}
        

}
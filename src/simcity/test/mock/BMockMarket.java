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
public class BMockMarket extends Mock implements BMarket{

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

        public BMockMarket(String name) {
                super(name);
                this.name = name;
        		
        		if(name=="market1")
                {
                    marketStock.put("steak",new BFood("steak", 50, 5,50 ,5,16,0));
                    marketStock.put("chicken",new BFood("chicken", 50,5,50,5,11,0));
                    marketStock.put("salad",new BFood("salad", 50, 5,50,5,6,0));
                    marketStock.put("pizza",new BFood("pizza", 50, 5,50,5,9,0));
                   
                }
                else if(name=="market2")
                {
                	marketStock.put("steak",new BFood("steak", 50, 5,5,5,16,0));
                    marketStock.put("chicken",new BFood("chicken", 50,5,50,5,11,0));
                    marketStock.put("salad",new BFood("salad", 50, 5,50,5,6,0));
                    marketStock.put("pizza",new BFood("pizza", 50, 5,50,5,9,0));
                	
                }
                else if(name=="market3")
                {
                	marketStock.put("steak",new BFood("steak", 50, 5,50,5,16,0));
                    marketStock.put("chicken",new BFood("chicken", 50,5,50,5,11,0));
                    marketStock.put("salad",new BFood("salad", 50, 5,50,5,6,0));
                    marketStock.put("pizza",new BFood("pizza", 50, 5,50,5,9,0));
                	
                }

        }

        public void setCashier(BCashier cashier){
        	this.cashier=cashier;
        }
    	public  void msgPaytoMarket(BCheck check){
    		
    		log.add(new LoggedEvent("Received msgPaytoMarket from Cashier"));
    	}
    	public  void msgHereisCookOrder(BCook cook, List<BFood> orderFromCook){}
        

}
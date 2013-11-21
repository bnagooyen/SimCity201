package simcity.BRestaurant;

import agent.Agent;

import java.util.*;

import restaurant.interfaces.BCashier;
import restaurant.interfaces.BCook;
import restaurant.interfaces.BMarket;




public class BMarketRole extends Agent implements BMarket{
	private Map<String,BFood> marketStock=new HashMap <String,BFood>();
	 private List<BFood> orderFromCook=new ArrayList<BFood>();
	 private List<BCheck> myChecks=Collections.synchronizedList(new ArrayList<BCheck>());
	 private String name;
	 private BCook cook;
	 public cookState state; 
	 private BCashier cashier;
	 private int totalMoney=0;
	 
	 
	 public enum cookState
	 {noAction, orderMade, needToOrder, orderCompleted};
	
	public boolean orderEntered=false;
	
	
	
	
	public BMarketRole(String name) {
		super();

		this.name = name;
		
		if(name=="market1")
        {
            marketStock.put("steak",new BFood("steak", 50, 5,3 ,5,16,0));
            marketStock.put("chicken",new BFood("chicken", 50,5,50,5,11,0));
            marketStock.put("salad",new BFood("salad", 50, 5,50,5,6,0));
            marketStock.put("pizza",new BFood("pizza", 50, 5,50,5,9,0));
           
        }
        else if(name=="market2")
        {
        	marketStock.put("steak",new BFood("steak", 50, 5,50,5,16,0));
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
		this.cashier = cashier;
    }
	
	
	//scheduler
	
	protected boolean pickAndExecuteAnAction() {

        if(orderEntered==true) 
        {
            
            completeOrder();
            
            return true;
        }
        
        synchronized(myChecks){
        for (BCheck thisCheck : myChecks){
        	if (thisCheck.paidbyCashier==true){
        		takeMoney(thisCheck);
        	}
        }
        }
    	return false;
    }
	
	
	
	//Messages
	
	public void msgHereisCookOrder(BCook cook, List<BFood> orderFromCook)
    {
		orderEntered = true;
		this.cook = cook;
        this.orderFromCook = orderFromCook;
        stateChanged();
    }
	
	public void msgPaytoMarket(BCheck check){
		myChecks.add(check);
		stateChanged();
	}
	
	
	
	//Actions
	private void completeOrder(){
		 
	        
	        for(int i=0; i < orderFromCook.size(); i++)
	        {
	            if(marketStock.get(orderFromCook.get(i).typeOfFood).quantity> orderFromCook.get(i).amount)
	            {
	            	if (orderEntered==true){
	                marketStock.get(orderFromCook.get(i).typeOfFood).quantity = marketStock.get(orderFromCook.get(i).typeOfFood).quantity-orderFromCook.get(i).amount;
	                cook.msgHereisCompletedOrder(orderFromCook);
	                
	                BCheck marketCheck=new BCheck(orderFromCook.get(i).typeOfFood, orderFromCook.get(i).amount);
	                
	                cashier.msgHereisCheckfromMarket(marketCheck, this);
	                orderEntered = false;
	            	}
	            }
	            
	            else if(marketStock.get(orderFromCook.get(i).typeOfFood).quantity < orderFromCook.get(i).amount){
	            	if(orderEntered=true){
	            		BCheck marketCheck=new BCheck(orderFromCook.get(i).typeOfFood, orderFromCook.get(i).amount-marketStock.get(orderFromCook.get(i).typeOfFood).quantity);
		                
		                cashier.msgHereisCheckfromMarket(marketCheck, this);
	            	cook.msgCannotCompleteOrder();
	            	orderEntered=false;
	            	}
	            }
	          
	        }
	        
	        
	  }
	
	private void takeMoney(BCheck check){
		totalMoney+=check.price;
		myChecks.remove(check);
		
		
		
	}
	
	
}
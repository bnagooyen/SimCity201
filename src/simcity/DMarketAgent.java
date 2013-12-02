package simcity;

import agent.Agent;
import simcity.interfaces.Cashier;
import simcity.interfaces.Market;
import simcity.DMarketAgent.InventoryOrder.InventoryOrderState;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class DMarketAgent extends Agent implements Market {
	private int inventoryAmnt=4;
	Timer timer = new Timer();
	private int  mktNum;
	public Map<String, DFood> marketFood = new HashMap<String, DFood>();
//	private List<InventoryOrder> myOrders = Collections.synchronizedList(new ArrayList<InventoryOrder>());
	private List<InventoryOrder> myOrders = new ArrayList<InventoryOrder>();
	//public HostGui hostGui = null;
	private Map<String, Double> prices = new HashMap<String, Double>();
	private Semaphore atTable = new Semaphore(0,true);
	private double registerVal;
	private DCookRole cook;
	//should be a list in time
	
	//ArrayList<Order> orders = new ArrayList<Order>();

	public DMarketAgent(int num) {
		super();
		this.mktNum = num;
		
		//GRADER: CHANGE MARKET VALS HERE
		//marketFood.put("Chicken", new Food("Chicken", 5, X));
		
		marketFood.put("Chicken", new DFood("Chicken", 5, inventoryAmnt));
		marketFood.put("Steak", new DFood("Steak", 10, inventoryAmnt));
		marketFood.put("Salad", new DFood("Salad", 3, inventoryAmnt));
		marketFood.put("Pizza", new DFood("Pizza", 6, inventoryAmnt));
		
		System.out.println("Market "+mktNum+" amount initialized to "+ inventoryAmnt);
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
		registerVal=0;
	}


	// The animation DoXYZ() routines
	

	public int getMktNum() {
		return mktNum;
	}

	// Messages

	//hack!
	public void msgAddCook(DCookRole c) {
		cook=c;
		//System.out.println("added cook");
	}
	
	public void msgIncMarketAmnt() {
		inventoryAmnt++;
		marketFood.put("Chicken", new DFood("Chicken", 5, inventoryAmnt));
		marketFood.put("Steak", new DFood("Steak", 10, inventoryAmnt));
		marketFood.put("Salad", new DFood("Salad", 3, inventoryAmnt));
		marketFood.put("Pizza", new DFood("Pizza", 6, inventoryAmnt));
		System.out.println("Market "+mktNum+" amount updated to "+ inventoryAmnt);
	}
	
	public void msgDecMarketAmnt() {
		if(inventoryAmnt>0) {
			inventoryAmnt--;
			marketFood.put("Chicken", new DFood("Chicken", 5, inventoryAmnt));
			marketFood.put("Steak", new DFood("Steak", 10, inventoryAmnt));
			marketFood.put("Salad", new DFood("Salad", 3, inventoryAmnt));
			marketFood.put("Pizza", new DFood("Pizza", 6, inventoryAmnt));
			System.out.println("Market "+mktNum+" amount updated to "+ inventoryAmnt);
		}
		else {
			System.out.println("Can't lower than 0!");
		}
	}
	
	@Override
	public void msgHereIsAnInventoryOrder(ArrayList<simcity.DFoodOrder> orderToMarket, int id, Cashier c) {
		System.out.println("new order added w/ cahsier"+ c);
		myOrders.add(new InventoryOrder(orderToMarket, id, c));
		stateChanged();
	}
	
	@Override
	public void msgHereIsAPayment(double val, Cashier ca) {
		System.out.println("received payment from casheir for $" + val);
		for(InventoryOrder order: myOrders) {
			if(order.c==ca) {
				order.pmtVal=val;
				order.state=InventoryOrderState.receivedPayment;
				stateChanged();	
			}
		}
	
	}
	//utilities

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		try {
		
		for(int i=0; i<myOrders.size(); i++) {
			if(myOrders.get(i).state==InventoryOrderState.pending) {
				ProcessOrder(myOrders.get(i));
				return true;
			}
		}
		
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions
	
/*		
	private void ProcessPayment(InventoryOrder ord){
		registerVal+=ord.pmtVal;
		System.out.println("****Market " + mktNum+" register updated to : " + registerVal);
		myOrders.remove(ord);
	}
	*/
	private void ProcessOrder(final InventoryOrder tobeprocessed) {
//		  System.out.println("processorder start");
			ArrayList<DFoodOrder> orderList=tobeprocessed.getOrder();
		  ArrayList<DFoodOrder> deliveryList = new ArrayList<DFoodOrder>();
		  ArrayList<DFoodOrder> notFulfilledList = new ArrayList<DFoodOrder>();
		  
		  for(DFoodOrder foo: orderList) {
			  DFood temp = marketFood.get(foo.getFood());
			  if(foo.getVal()<= temp.getAmount()) { // order can be met fully
				  System.out.print("market "+ mktNum+ " can meet your order!\n");
				  temp.setAmount(temp.getAmount()-foo.getVal());
				  marketFood.put(foo.getFood(), temp);
				  deliveryList.add(foo); // adds full order to delivery list
			  }
			  else { //part of order could not be fulfilled
				  if(temp.getAmount()>0) { // still have some left
					  deliveryList.add(new DFoodOrder(foo.getFood(), temp.getAmount()));
					  notFulfilledList.add(new DFoodOrder(foo.getFood(), foo.getVal()-temp.getAmount())); //wtv couldn't fulfill
				  }
				  else
					  notFulfilledList.add(foo);
			  }
		  }
		  tobeprocessed.state=InventoryOrderState.processed;
		  if(notFulfilledList.size()>0) { // had something that wasn't complete
			  cook.msgCouldNotFulfillThese(notFulfilledList, tobeprocessed.getID());
		  }
		  if(deliveryList.size()>0) { // have something to send
			  //computing bill
			  final double bill = ComputeBill(deliveryList);
			  System.out.println("bill = "+ bill);
			  final ArrayList<DFoodOrder> forTimer = deliveryList;
			  final Market forT=this;
			  timer.schedule(new TimerTask() {
				  @Override
				public void run() {
					  cook.msgHereIsYourFoodOrder(forTimer);
					  ((DCashierRole) tobeprocessed.c).msgHereIsAnInventoryBill(bill, forT);
					  System.out.println("sent bill to cashier for " + bill+ "!");
					  //myOrders.remove(tobeprocessed);
					  tobeprocessed.state=InventoryOrderState.waitingForPayment;
				  }
			  }, 2000);
		  }
		  
	}
	  
	  private double ComputeBill(ArrayList<DFoodOrder> tbd) {
		  DecimalFormat df = new DecimalFormat("###.##");
		  double bill =0;
		  for (DFoodOrder ord: tbd) {
			  //System.err.println(ord.getVal()*prices.get(ord.getFood()));
			  bill+=ord.getVal()*prices.get(ord.getFood());
		  }
		  bill=Double.parseDouble(df.format(bill));
		  return bill;
	  }

		static class InventoryOrder {
			ArrayList<DFoodOrder> order;
			Cashier c;
			double pmtVal;
			double amnt;
			enum InventoryOrderState { pending, processed, waitingForPayment, receivedPayment};
			InventoryOrderState state;
			int ORDERID;
			
			InventoryOrder(ArrayList<DFoodOrder> myOrder, int orderid, Cashier ca) {
				order = myOrder;
				state= InventoryOrderState.pending;
				ORDERID=orderid;
				c=ca;
				amnt=0;
				pmtVal=0;
			}
			
			public ArrayList<DFoodOrder> getOrder() {
				return order;
			}
			
			int getID() {
				return ORDERID;
			}
		}
		
	
}




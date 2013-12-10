package simcity.LRestaurant;

import agent.Role;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LHostRole.MyCustomers;
import simcity.LRestaurant.LHostRole.myWaiter;
import simcity.LRestaurant.ProducerConsumerMonitor;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LWaiterRole.WaiterState;
import simcity.LRestaurant.gui.LCookGui;
import simcity.LRestaurant.gui.LWaiterGui;
import simcity.LRestaurant.gui.LHostGui;
import simcity.PersonAgent;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Host;
import simcity.interfaces.LCook;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LHost;
import simcity.interfaces.LWaiter;
import simcity.interfaces.MarketManager;

import java.util.*;
//import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LHostRole extends Role implements LHost, Host {
        static final int NTABLES = 4;//a global for the number of tables.
        //Notice that we implement waitingCustomers using ArrayList, but type it
        //with List semantics.
        private List<MyCustomers> customers = Collections.synchronizedList(new ArrayList<MyCustomers>());
        private List<myWaiter> waiters = Collections.synchronizedList(new ArrayList<myWaiter>());
//      public List<MarketManager> markets =Collections.synchronizedList( new ArrayList<MarketManager>()); 
        private Collection<Table> tables;
        int count = -1; //lining customers up
        int countW = -1; //lining waiters up
        //note that tables is typed with Collection semantics.
        //Later we will see how it is implemented

        private ProducerConsumerMonitor theMonitor;
        LCookRole cook;
        LCookGui cookGui;
        LCashierRole cashier;
        
        private int hour;
        private boolean isClosed;
        private double restaurantMoney;
        
        public enum CustomerState {waiting, assigned, seated, deciding, thinking, left};

        public enum WaiterState {working, wantsToGoOnBreak, isOnBreak};
        
        boolean moveWaitLine = false;

        private String name;
        Timer timer = new Timer();

        public LHostGui hostGui = null;

        public LHostRole() {
                //super(p);

                //this.name = p.getName();
                // make some tables
                tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
                for (int ix = 1; ix <= NTABLES; ix++) {
                        tables.add(new Table(ix));//how you add to a collections
                }
                
                theMonitor = new ProducerConsumerMonitor();

        }
        
//        public void addMarket(MarketManager m){
//    		markets.add(m);
//    	}

//        public void addWaiter(LWaiterRole w){
//                waiters.add(new myWaiter(w,0));//newly hired waiter 
//                //int count = -1;
//                synchronized(waiters){
//                        for(myWaiter wait:waiters){
//                                if(wait.state == WaiterState.working){
//                                        countW++;
//                                }
//                                if(wait.w == w){
//                                        w.waiterGui.DoWait(countW);
//                                }
//                        }
//                        countW = -1;
//                }
//                stateChanged();
//        }

        public String getMaitreDName() {
                return name;
        }

        public String getName() {
                return name;
        }

        public List<MyCustomers> getCustomers() {
                return customers;
        }

        public Collection<Table> getTables() {
                return tables;
        }
        // Messages

        public void msgHereIsMoney(double money){
    		restaurantMoney += money;
    	}
    	
        
        public void msgTimeUpdate(int hour){
    		this.hour = hour;
    		if(hour == 9) {
    			restaurantMoney = 50000.0;
    		}
    	}
        
        public void msgIAmHere(Role r, String type){
    		
    		if(type.equals("waiter")){
    			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Waiter is here");
    			Do("Waiter is here");
    			waiters.add(new myWaiter((LWaiterRole) r,0, cookGui));
    		}
    		else if(type.equals("cook")){
    			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Cook is here");
    			Do("Cook is here");
    			cook = (LCookRole)r;
    		}
    		else if(type.equals("cashier")){
    			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Cashier is here");
    			Do("Cashier is here");
    			cashier = (LCashierRole) r;
    		}
    		
    		if(!waiters.isEmpty() && cook != null && cashier != null){
    			isClosed = false;
    		}
    		else{
    			isClosed = true;
    		}
    		
    		stateChanged();
    	}
        
        public void msgIAmHere(Role r, String type,LCookGui cookGui) {
			// TODO Auto-generated method stub
        	if(type.equals("cook")){
    			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Cook is here");
    			Do("Cook is here");
    			cook = (LCookRole)r;
    			this.cookGui = cookGui;
    		}
        	
        	for(myWaiter w : waiters){
        		w.setCookGui(cookGui);
        	}
		}
        
        public void msgLeftLine(){
                moveWaitLine = true;
                stateChanged();
        }
        
        public void msgChoseToLeave(LCustomer c){
                synchronized(customers){
                        for(MyCustomers cust:customers){
                                if(cust.c == c){
                                        //waitingCustomers.remove(cust);
                                        //cust.c.customerGui.DoExitRestaurant();
                                        cust.state = CustomerState.left;
                                }
                        }
                }
        }

        public void msgChoseToWait(LCustomer c){
                synchronized(customers){
                        for(MyCustomers cust:customers){
                                if(cust.c == c){
                                        cust.state = CustomerState.waiting;
                                }
                        }
                }
        }

        public void msgReadyToWork(LWaiterRole waiterRole){
        		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Waiter is back");
                Do("Waiter is back");
                
                synchronized(waiters){
                        for (myWaiter w:waiters) {
                                if (w.w == waiterRole) {
                                        w.state = WaiterState.working;
                                }
                        }
                }
                stateChanged();
        }

        public void msgWantToGoOnBreak(LWaiterRole waiter){
        		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Waiter ask to go on break");
                Do("Waiter ask to go on break");
                
                synchronized(waiters){
                        for (myWaiter w:waiters) {
                                if (w.w == waiter) {
                                        w.state = WaiterState.wantsToGoOnBreak;
                                }
                        }
                }
                stateChanged();
        }

        public void msgIWantToEat(LCustomer c) {
                customers.add(new MyCustomers(c));
                //waitingCustomers.add(new MyCustomers(c));
            
                int counter = 0;
                
                synchronized(customers){
                        for(MyCustomers cust:customers){
                                if(cust.state.equals(CustomerState.seated)){
                                        counter ++;
                                }
                        }
                }
                
                if(counter >= tables.size()){
                        synchronized(customers){
                                for(MyCustomers cust:customers){
                                        if(cust.c == c){
                                                cust.state = CustomerState.deciding;
                                        }
                                }
                        }
                }

                stateChanged();
        }


        public void msgEmptyTable(int tableNum, LWaiterRole waiter, LCustomer c) {
        	synchronized(customers){
                for(MyCustomers cust:customers){
                        if(cust.c == c){
                        	cust.state = CustomerState.left;
                        }
                }
        	}
 
        	
                synchronized(tables){
                        for (Table table : tables) {
                                if (table.tableNumber == tableNum){
                                        table.setUnoccupied();
                                }
                        }
                }
                
                synchronized(waiters){
                        for (myWaiter w:waiters) {
                                if (w.w == waiter) {
                                        w.numCust--;//customer left
                                }
                        }
                }
                stateChanged();
        }

        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
        		
        	if(hour == myPerson.directory.get(myPerson.jobLocation).closeHour+1 && !isClosed){
    			closeRestaurant();
    			return true;
    		}
    		
    		if(hour == myPerson.directory.get(myPerson.jobLocation).closeHour && !customers.isEmpty()){
    			restaurantClosed();
    			return true;
    		}
    		
//    		if(hour == 11 && !isClosed){ //restaurant opens hour
//    			tellOpen();
//    			return true;
//    		}

                synchronized(customers){
                        for(MyCustomers c: customers){
                                if(c.state == CustomerState.deciding){
                                        fullRestaurant(c);
                                }
                        }
                }
                
                synchronized(waiters){
                        for (myWaiter w:waiters) {
                                if (w.state == WaiterState.wantsToGoOnBreak) {
                                        checkIfCan(w.w);
                                }
                        }
                }

                /* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
                 */

                int tempTab = -1;
                int min = 0;


                if(waiters.isEmpty()){
                        return false;
                }
                //Find waiter with least amount of customers
                for(int i = 0; i<waiters.size(); i++){
                        //System.out.println("Waiter " + i + ": " + waiters.get(i).numCust);
                        if(waiters.get(i).state == WaiterState.working){
                                if( (waiters.get(i).numCust) < (waiters.get(min).numCust)){
                                        min = i;
                                }
                        }
                }
                //System.out.println("Waiter with minimum customers is Waiter " + min);

                //Find Unoccupied Table
                synchronized(tables){
                        for (Table table : tables) {
                                if(!table.isOccupied()){
                                        tempTab = table.tableNumber;
                                }
                        }
                }
                
                if(tempTab == -1){
                        return false;
                }

                //Grab customer who is waiting and seat 
                synchronized(customers){
                        for(MyCustomers cust : customers){
                                if(cust.state == CustomerState.waiting){
                                        synchronized(tables){
                                                for (Table table : tables) {
                                                        if(table.tableNumber == tempTab){
                                                                table.isOccupied(cust.c);
                                                        }
                                                }
                                        }
                                        cust.state = CustomerState.assigned;
                                        seatCustomer(cust, tempTab, waiters.get(min).w);
                                        waiters.get(min).numCust++;//cust assigned to waiter
                                        return true;
                                }
                        }
                }


                return false;
                //we have tried all our rules and found
                //nothing to do. So return false to main loop of abstract agent
                //and wait.
        }

        // Actions

//        private void tellOpen(){
//        	synchronized(markets){
//    			for(MarketManager m : markets){
//    				m.msgRestaurantOpen(cook);
//    			}
//    		}
//        }
        
        private void closeRestaurant(){ //pay employees 50
        	AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Closing restaurant.");
    		Do("Closing restaurant. It is "+hour);
    		synchronized(waiters){
    			for(myWaiter w: waiters){
    				restaurantMoney -= 50;
    				w.w.msgGoHome(50);
    			}
    		}
    		
//    		synchronized(markets){
//    			for(MarketManager m : markets){
//    				m.msgRestaurantClosed(cook);
//    			}
//    		}
    		
    		cashier.msgGoHome(50);
    		cook.msgGoHome(50);
    		
    		waiters.clear();
    		cashier = null;
    		cook = null;
    		isClosed = true;
    		
    	}
    	
    	private void restaurantClosed() {
    		
    		synchronized(customers){
    			for(MyCustomers c: customers){
    				c.c.msgRestaurantClosed();
    			}
    			customers.clear();
    		}
    	}
    	
        
        private void fullRestaurant(MyCustomers c){
                        c.c.msgRestaurantIsFull();
                        c.state = CustomerState.thinking;
                        stateChanged();
        }

        private void checkIfCan(LWaiterRole w){
                int workingCount = 0;
                myWaiter mW = null;

                synchronized(waiters){
                        for (myWaiter waiter:waiters) {
                                if (waiter.state == WaiterState.working) {
                                        workingCount++;
                                }
                                if(waiter.w == w){
                                        mW = waiter;
                                }
                        }
                }

                if(workingCount > 0){
                		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Waiter can go on break");
                        Do("Waiter can go on break");
                        w.msgBreakReply(true);
                        mW.state = WaiterState.isOnBreak;
                }
                else{ 
                		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LHostRole", "Waiter cannot go on break");
                        Do("Waiter cannot go on break");
                        w.msgBreakReply(false);
                        mW.state = WaiterState.working;
                }
                stateChanged();
        }

        private void seatCustomer(MyCustomers customer, int table, LWaiterRole w) {
                //waitingCustomers.remove(customer);
                customer.state = CustomerState.seated;
                w.msgSeatCustomer(customer.c, table);        
        }
        


        //utilities
        public void addWaiter(LWaiterRole w) {
    		waiters.add(new myWaiter(w,0,cookGui));
    		if(w instanceof LWaiterSharedDataRole) {
    			((LWaiterSharedDataRole) w).setMonitor(theMonitor);
    		}
    		
    		 synchronized(waiters){
               for(myWaiter wait:waiters){
                       if(wait.state == WaiterState.working){
                               countW++;
                       }
                       if(wait.w == w){
                               w.waiterGui.DoWait(countW);
                       }
               }
               countW = -1;
       }
       stateChanged();
    	}

    	public void setCook(LCookRole c) {
    		cook = c;
    		c.setMonitor(theMonitor);
    	}
        

        public class myWaiter {
                LWaiterRole w;
                int numCust;
                WaiterState state;
                LCookGui cookGui;

                public myWaiter(LWaiterRole waiter, int num, LCookGui cg) {
                        w = waiter;
                        numCust = num;
                        state = WaiterState.working;
                        cookGui = cg;
//                        print("COOKGUI in myWaiter: " + cg);
                        w.getGui().setCookGui(cg);
                }
                
                public void setCookGui(LCookGui cg){
                	w.getGui().setCookGui(cg);
                }

        }

        public void setGui(LHostGui gui) {
                hostGui = gui;
        }

        public LHostGui getGui() {
                return hostGui;
        }

        public class MyCustomers {
                LCustomer c;
                CustomerState state;
                
                public MyCustomers(LCustomer c){
                        this.c = c;
                        state = CustomerState.waiting;
                }

        }

        public class Table {
                LCustomer occupiedBy;
                int tableNumber;

                public Table(int tableNumber) {
                        this.tableNumber = tableNumber;
                }

                void isOccupied(LCustomer c){
                        occupiedBy = c;
                }

                /*void setOccupant(CustomerAgent cust) {
                        occupiedBy = cust;
                }*/

                void setUnoccupied() {
                        occupiedBy = null;
                }

                /*CustomerAgent getOccupant() {
                        return occupiedBy;
                }*/

                boolean isOccupied() {
                        return occupiedBy != null;
                }

                public String toString() {
                        return "table " + tableNumber;
                }
        }

		@Override
		public void setCook(LCook cook) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addWaiter(LWaiter lw) {
			// TODO Auto-generated method stub
			
		}

	

}
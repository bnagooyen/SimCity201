package simcity.LRestaurant;

import agent.Role;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LWaiterRole.WaiterState;
import simcity.LRestaurant.gui.LWaiterGui;
import simcity.LRestaurant.gui.LHostGui;
import simcity.PersonAgent;
import simcity.interfaces.Host;
import simcity.interfaces.LCustomer;

import java.util.*;
//import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LHostRole extends Role implements Host {
        static final int NTABLES = 4;//a global for the number of tables.
        //Notice that we implement waitingCustomers using ArrayList, but type it
        //with List semantics.
        private List<MyCustomers> customers = Collections.synchronizedList(new ArrayList<MyCustomers>());
        private List<myWaiter> waiters = Collections.synchronizedList(new ArrayList<myWaiter>());
        private Collection<Table> tables;
        int count = -1; //lining customers up
        int countW = -1; //lining waiters up
        //note that tables is typed with Collection semantics.
        //Later we will see how it is implemented

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

        }

        public void addWaiter(LWaiterRole w){
                waiters.add(new myWaiter(w,0));//newly hired waiter 
                //int count = -1;
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
                        Do("Waiter can go on break");
                        w.msgBreakReply(true);
                        mW.state = WaiterState.isOnBreak;
                }
                else{ 
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

        public class myWaiter {
                LWaiterRole w;
                int numCust;
                WaiterState state;

                public myWaiter(LWaiterRole waiter, int num) {
                        w = waiter;
                        numCust = num;
                        state = WaiterState.working;
                }

        }

        public void setGui(LHostGui gui) {
                hostGui = gui;
        }

        public LHostGui getGui() {
                return hostGui;
        }

        private class MyCustomers {
                LCustomer c;
                CustomerState state;
                
                public MyCustomers(LCustomer c){
                        this.c = c;
                        state = CustomerState.waiting;
                }

        }

        private class Table {
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
}
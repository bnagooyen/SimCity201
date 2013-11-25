package simcity.LRestaurant;

import agent.Role;
import simcity.LRestaurant.LCookRole.OrderState;
import simcity.LRestaurant.LCustomerRole.AgentEvent;
import simcity.LRestaurant.gui.LWaiterGui;
import simcity.LRestaurant.LMenu;
import simcity.PersonAgent;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LWaiter;
import simcity.test.mock.EventLog;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class LWaiterRole extends Role implements LWaiter{
        static final int NTABLES = 3;//a global for the number of tables.
        //Notice that we implement waitingCustomers using ArrayList, but type it
        //with List semantics.
        public List<MyCustomers> customers =Collections.synchronizedList(new ArrayList<MyCustomers>());
        //note that tables is typed with Collection semantics.
        //Later we will see how it is implemented


        private String name;
        Timer timer = new Timer();
        private boolean onBreak;
        EventLog log;


        protected Semaphore task = new Semaphore(0,true);

        public LWaiterGui waiterGui = null;
        private LMenu m = new LMenu();
        public WaiterState waiterState;

        LCookRole cook;
        LHostRole host;
        LCashierRole cashier;

        public enum WaiterState{working, wantBreak, askingBreak, canGoOn, onBreak, checkingIn, backFromBreak};

        public enum CustomerState{waiting, seated, readyToOrder, askOrder, ordered, reorder, newChoice, waitForFood, orderIsReady, served, receivingCheck, wantsCheck, waitingOnCashier, gotCheck, leaving, gone};

        int numCust = 0;

        public LWaiterRole(PersonAgent p) {
                super(p);
                this.name = p.getName();
                onBreak = false;
                waiterState = WaiterState.working;
                log = new EventLog();
        }

//        public WaiterRole() {
//                super();
//        }

        public void setCashier(LCashierRole cashier){
                this.cashier = cashier;
        }

        public void setCook(LCookRole cook){
                this.cook = cook;
                waiterGui.setCookGui(cook.cookGui);
        }

        public void setHost(LHostRole host){
                this.host = host;
        }

        public String getMaitreDName() {
                return name;
        }

        public String getName() {
                return name;
        }

        public List <MyCustomers> getCustomers() {
                return customers;
        }

        public boolean isOnBreak(){
                if(waiterState == WaiterState.onBreak){
                        return true;
                }
                else{
                        return false;
                }
        }

        public void setOnBreak(){
                waiterState = WaiterState.wantBreak;
                stateChanged();
        }



        // Messages

        public void msgBreakReply(boolean reply){
                onBreak = reply;
                if(reply == true){
                        waiterState = WaiterState.canGoOn;
                }
                stateChanged();
        }

        public void msgSeatCustomer(LCustomer cust, int table) {
//                waiterGui.setWorking();
//                waiterState = WaiterState.working;
                customers.add(new MyCustomers(cust, table, CustomerState.waiting));
                //System.out.println("Waiter " + this.name + " adds customer" + cust.getCustomerName() + " to list.");
                stateChanged();
        }

        public void msgReadyToOrder(LCustomer cust) {
                synchronized(customers){
                        for(MyCustomers c : customers){
                                if(c.c == cust){
                                        c.state = CustomerState.readyToOrder;
                                }
                        }
                }
                print("Received order from " + cust);
                stateChanged();
        }

        public void msgHereIsMyChoice(LCustomer cust, String choice) {
                synchronized(customers){
                        for(MyCustomers c : customers){
                                if(c.c == cust){
                                        c.choice = choice;
                                        c.state = CustomerState.ordered;
                                        print("Received " + cust + "'s order");
                                }
                        }
                }
                stateChanged();
        }

        public void msgRanOutofFood(int table, String choice) {
                synchronized(customers){
                        for(MyCustomers c : customers){
                                if(c.table == table){
                                        c.state = CustomerState.reorder;
                                }
                        }
                }
                m.removeItem(choice);
                stateChanged();
        }

        public void msgOrderIsReady(int table, String choice) {
                synchronized(customers){
                        for(MyCustomers c : customers){
                                if(c.table == table){
                                        c.state = CustomerState.orderIsReady;
                                }
                        }
                }
                stateChanged();
        }

        public void msgReadyForCheck(LCustomer cust){
                synchronized(customers){
                        for(MyCustomers c : customers){
                                if(c.c == cust){
                                        c.state = CustomerState.wantsCheck;
                                }
                        }
                }
                stateChanged();
        }

        public void msgHereIsCheck(int check, LCustomer cust){
                synchronized(customers){
                        for(MyCustomers c : customers){
                                if(c.c == cust){
                                        c.check = check;
                                        c.state = CustomerState.receivingCheck;
                                }
                        }
                }
                stateChanged();
        }

        public void msgDoneEatingAndLeaving(LCustomer cust) {
                synchronized(customers){        
                        for(MyCustomers c : customers){
                                if(c.c == cust){
                                        c.state = CustomerState.leaving;
                                }
                        }
                }
                stateChanged();
        }


        public void msgTask() {
                task.release();
                stateChanged();
        }

        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
                if(waiterState.equals(WaiterState.wantBreak)){
                        askHostForBreak();
                        return true;
                }

                if(waiterState.equals(WaiterState.backFromBreak)){
                        tellHostBack();
                        return true;
                }

                synchronized(customers){
                        for (MyCustomers c : customers) {
                                if(c.state == CustomerState.orderIsReady){
                                        serveOrder(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {
                                if(c.state == CustomerState.waiting){
                                        seatCustomer(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {        
                                if(c.state == CustomerState.readyToOrder){
                                        takeOrder(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {        
                                if(c.state == CustomerState.reorder){
                                        giveNewMenu(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {        
                                if(c.state == CustomerState.ordered){
                                        giveCookOrder(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {
                                if(c.state == CustomerState.wantsCheck){
                                        tellCashier(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {
                                if(c.state == CustomerState.receivingCheck){
                                        giveCustCheck(c);
                                        return true;
                                }
                        }
                        for (MyCustomers c : customers) {
                                if(c.state == CustomerState.leaving){
                                        cleanTable(c);
                                        return true;
                                }
                        }
        
                        int custLeft = 0;
                        for (MyCustomers c : customers) {
                                if(c.state == CustomerState.leaving){ //fix statement since waiter does not go to clean up table...must have another state?
                                        custLeft++;
                                }
                        }
                        if(custLeft == customers.size() && waiterState == WaiterState.canGoOn){
                                goOnBreak();
                                return true;
                        }
                }
                return false;
        }

        // Actions

        private void tellHostBack(){
                Do("Back from Break");
                onBreak = false;
                host.msgReadyToWork(this);
                waiterState = WaiterState.checkingIn;
                stateChanged();
        }

        private void goOnBreak() {
                Do("Going on Break");
                waiterState = WaiterState.onBreak;
                /**
                waiterGui.DoLeaveCustomer();
                waiterGui.DoGoOnBreak();
                try {
                    task.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                */
                timer.schedule(new TimerTask() {
                        public void run() {
                                waiterState = WaiterState.backFromBreak;
                                waiterGui.DoLeaveCustomer();
                                stateChanged();
                        }
                },
                15000);
       
        }

        private void askHostForBreak() {
                Do("Asking for Break");
                host.msgWantToGoOnBreak(this);
                waiterState = WaiterState.askingBreak;
        }

        private void seatCustomer(MyCustomers c) {
        		/**
                waiterGui.DoGetCustomer();
                c.state = CustomerState.seated;

                //print("Asking for a semaphore. seatCustomer 1");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                */
                //print("Got a semaphore");
                print("Seating " + c.c.getCustomerName() + " at table " + c.table);
                c.c.msgFollowMe(c.table,m, this);
                /**
                waiterGui.DoGoToTable(c.table); 
                //print("Asking for a semaphore. seatCustomer 2");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                */
                host.msgLeftLine();
                //print("Got a semaphore");
                //waiterGui.DoLeaveCustomer();
                numCust++;
                //print("Waiter " + this.name + " seats " + c.c.getCustomerName() + ". NumCust is " + numCust);
                print("Waiter " + this.name + " seats " + c.c.getCustomerName() + ".");
        }

        private void giveNewMenu(MyCustomers c) {
                Do("Giving customer new menu.");
                /**
                waiterGui.DoGoToTable(c.table); 
                //print("Asking for a semaphore. seatCustomer 2");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                */
                //print("Got a semaphore");
                c.c.msgPleaseReorder(m);
                //waiterGui.DoLeaveCustomer();
                c.state = CustomerState.newChoice;
        }

        private void takeOrder(MyCustomers c){
        	/**
                waiterGui.DoGoToTable(c.table);

                //print("Asking for a semaphore. takeOrder1");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                //print("Got a semaphore");
		*/
                c.c.msgWhatWouldYouLike();
                //print("Asking " + c.c + " for their order.");
                c.state = CustomerState.askOrder;
        }


        protected abstract void giveCookOrder(MyCustomers c);//{
//                waiterGui.DoGoToCook();
//                Do("Giving cook order");
//                c.state = CustomerState.waitForFood;
//                //print("Asking for a semaphore. CookOrder1");
//                try {
//                        task.acquire();
//                } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                }
//                //print("Got a semaphore");
//                cook.msgHereIsAnOrder(c.table, c.choice, this);
//        }

        private void serveOrder(final MyCustomers c){
            	/**
                waiterGui.DoGoToPlating();
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                print("Serving food to " + c.c);
                waiterGui.DoGoToTable(c.table);
                waiterGui.setFood(c.choice);
                waiterGui.gotFood();

                //print("Asking for a semaphore. serveOrder1");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                //print("Got a semaphore");
                //waiterGui.gaveFood();
                //waiterGui.DoLeaveCustomer();
                 */
                c.c.msgHereIsFood();
                c.state = CustomerState.served;
        }

        private void tellCashier(final MyCustomers c){
        		/**
                waiterGui.DoGoToTable(c.table);
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                */
                Do("Asking cashier for check");
                /**
                waiterGui.DoLeaveCustomer();
                waiterGui.DoGoToCashier();
                print("Asking for a semaphore. takeOrder1");
                                try {
                                        task.acquire();
                                } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                                //print("Got a semaphore");
                                 * 
                                 */
                cashier.msgComputeCheck(c.choice, c.c, this);
                c.state = CustomerState.waitingOnCashier;
        }

        private void giveCustCheck(final MyCustomers c){
                Do("Giving Cust Check");
                /**
                waiterGui.DoGoToTable(c.table);

                //print("Asking for a semaphore. takeOrder1");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                //print("Got a semaphore");
                 */
                c.c.msgHereIsCheck(c.check); //Create msgHereIsCheck
                c.state = CustomerState.gotCheck;
                //waiterGui.DoLeaveCustomer();
        }

        private void cleanTable(final MyCustomers c){
                Do("Cleaning the table");
                /**
                //waiterGui.DoGoToTable(c.table);
                //print("Asking for a semaphore. cleanTable1");
                try {
                        task.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                //print("Got a semaphore");

                waiterGui.DoLeaveCustomer();
                */
                host.msgEmptyTable(c.table, this, c.c);
                customers.remove(c);
                numCust--;
                //System.out.println(this.name + ": Customer " + c.c + " has left. NumCust is now " + numCust);
        }

        //utilities

        public void setGui(LWaiterGui gui) {
                waiterGui = gui;
        }

        public LWaiterGui getGui() {
                return waiterGui;
        }

        public class MyCustomers{
                LCustomer c;
                int table;
                String choice;
                CustomerState state;
                int check;

                MyCustomers(LCustomer cust,int t, CustomerState s){
                        c = cust;
                        table = t;
                        state = s;
                        check = 0;
                }
        }
}
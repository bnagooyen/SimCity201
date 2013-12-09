package simcity.BRestaurant;

import agent.Agent;
import agent.Role;
import simcity.PersonAgent;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.*;
import simcity.BRestaurant.*;
import simcity.BRestaurant.gui.BHostGui;
import simcity.LRestaurant.gui.LCookGui;
import simcity.PersonAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class BWaiterRole extends Role implements BWaiter{
        static final int NTABLES = 3;//a global for the number of tables.
        //Notice that we implement waitingCustomers using ArrayList, but type it
        //with List semantics.
        public List<BCustomer> waitingCustomers = new ArrayList<BCustomer>();
        boolean arrived;
        public List<String> myOrders = new ArrayList<String>();
        public int customerTable;
        public String customerOrder;
        private BHostRole host;
        protected Semaphore atCook = new Semaphore(0, true);
        private boolean requestBreak;
        private boolean onBreak=false;
        Timer timer= new Timer();
        SimCityGui gui;
//        BWaiterGui waitergui; MAKE BWAITERGUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        public enum customerState{
                needtobeSeated, readytoOrder, orderPending, orderisReady, done, reorder, noAction, needCheck, leave
        };


        public WaiterState mystate;
    
    public enum WaiterState 
    {
            working, wantToGoOnBreak, askingToGoOnBreak, canGoOnBreak, onBreak, backFromBreak, leave, unavailable
    }
    
    
        public List<myCustomer> myCustomers = new ArrayList<myCustomer>();

        public class myCustomer{
                public BCustomer c;
                public int tablenumber;
                public String choice;
                public customerState cusState;
        }



        public Collection<Table> tables;
        //note that tables is typed with Collection semantics.
        //Later we will see how it is implemented

        private String name;
        private Semaphore atTable = new Semaphore(0,true);

        public BHostGui hostGui = null;
        public BCook cook;
        private BCashier cashier;




        



        

        public BWaiterRole() {
                //super(p);
        	
        		//gui =g;
                this.name = name;
                // make some tables
                tables = new ArrayList<Table>(NTABLES);
                for (int ix = 1; ix <= NTABLES; ix++) {
                        tables.add(new Table(ix));//how you add to a collections
                }
        }

        public void setCook(BCook cook) {
                this.cook = cook;
                
        }

        public void msgSitCustomerAtTable(BCustomer customer, int tableNum){

                myCustomer thisCustomer=new myCustomer();
                thisCustomer.c=customer;
                thisCustomer.tablenumber=tableNum;
                thisCustomer.cusState=customerState.needtobeSeated;
                myCustomers.add(thisCustomer);
        }




        public String getMaitreDName() {
                return name;
        }

        public String getName() {
                return name;
        }

        public List getWaitingCustomers() {
                return waitingCustomers;
        }

        public Collection getTables() {
                return tables;
        }
        // Messages

        public void msgIWantFood(BCustomer cust) {
                waitingCustomers.add(cust);
//                if(waiterGui == null){
//        			waiterGui = new BWaiterGui(this, "BWaiterGui");
//        			gui.myPanels.get("Restaurant 5").panel.addGui(waiterGui);
//        		}
                stateChanged();
        }
        
        public void msgBreakAllowed(){
                onBreak=true;
                stateChanged();
        }
        
        public void msgBreakNotAllowed(){
                onBreak=false;
                stateChanged();
        }

           public void msgGoHome(double paycheck) {
        	AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "told to go home");
           Do("told to go home");
           myPerson.money += paycheck;
           mystate = WaiterState.leave;
           stateChanged();
   }
        public void msgLeavingTable(BCustomer cust) {
                for (Table table : tables) {
                        if (table.getOccupant() == cust) {
                                print(cust + " leaving " + table);
                                table.setUnoccupied();
                                stateChanged();
                        }
                }
        }

        public void msgAtTable() {//from animation
                //print("msgAtTable() called");
                atTable.release();// = true;
                stateChanged();
        }

        

        public void msgHereismyChoice(BCustomer customer, String choice){
                for(myCustomer thiscustomer : myCustomers){
                        if(thiscustomer.c==customer){

                                thiscustomer.choice=choice;
                                thiscustomer.cusState=customerState.orderPending;
                                stateChanged();
                        }
                }
        }
        
        public void msgOutOfThatFood(int tablenumber)
           {
                for(myCustomer customer:myCustomers){
                        if(customer.tablenumber == tablenumber){
                                customer.cusState = customerState.reorder;
                                stateChanged();
                                
                        }
                }          
           }
        
        public void msgOrderisReady(int tablenumber, String choice){
                for (myCustomer thiscustomer : myCustomers){
                        if(thiscustomer.tablenumber==tablenumber){
                                thiscustomer.cusState=customerState.orderisReady;

                                stateChanged();
                        }
                }
        }
        
        public void msgIWantCheck(BCustomer customer){
                for(myCustomer thiscustomer : myCustomers){
                        if(thiscustomer.c==customer){
                                thiscustomer.cusState=customerState.needCheck;
                                print("checktest");
                                stateChanged();
                        }
                }
                
        }
        
        public void msgCustomerNoMoney(){
                hostGui.DoLeaveCustomer();
                stateChanged();
        }

        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
                /* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
                 */
                for (Table table : tables) {
                
                        
                        if(arrived){
                                tellHost();
                        }
                        
                        try{
                        for (myCustomer customer : myCustomers) {
                                if (customer.cusState==customerState.readytoOrder) {
                                        takecustomerOrder(customer);
                                        return true;
                                }
                        }
                        }
                        catch(ConcurrentModificationException e) {
                                return false;
                        }
                        try{
                        for (myCustomer customer : myCustomers) {
                                if (customer.cusState==customerState.orderPending) {
                                        giveCookOrder(customer);
                                        return true;
                                }
                        }
                        }
                        catch(ConcurrentModificationException e) {
                                return false;
                        }
                        try{
                        for (myCustomer customer : myCustomers) {
                                if (customer.cusState==customerState.orderisReady) {
                                        giveFood(customer);
                                        return true;
                                }
                        }
                        }
                        catch(ConcurrentModificationException e) {
                                return false;
                        }
                        try{
                        for (myCustomer customer : myCustomers) {
                                if (customer.cusState==customerState.reorder) {
                                        needToReorder(customer);
                                        return true;
                                }
                        }
                        }
                        catch(ConcurrentModificationException e) {
                                return false;
                        }
                        try{
                        for (myCustomer customer : myCustomers) {
                                if (customer.cusState==customerState.needCheck) {
                                        giveCheck(customer);
                                        return true;
                                }
                        }
                        }
                        catch(ConcurrentModificationException e) {
                                return false;
                        }
                        if(onBreak==true){
                                takeBreak();
                                timer.schedule(new TimerTask() {

                                        public void run() {
                                                returnFromBreak();

                                                }
                                        },        
                                10000);
                        }
                        return true;


                }
                
                if(mystate==WaiterState.leave){
                        goHome();
                }

                return false;
                //we have tried all our rules and found
                //nothing to do. So return false to main loop of abstract agent
                //and wait.
        }

        // Actions

        public void seatCustomer(BCustomer customer, int tablenumber) {
                customer.msgSitAtTable(tablenumber, new BMenu(), this);

                //DoSeatCustomer(customer, tablenumber);
                try {
                        atTable.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                
                hostGui.DoLeaveCustomer();


        }
        
        private void takeBreak(){
                onBreak=false;
                host.msgPutMeOnBreak(this);
            	AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "taking break");
                print("taking break");
                
        }
        
         private void tellHost() {
         AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "telling manager I can work");
         Do("telling manager I can work");
         arrived = false;
         host.msgIAmHere(this, "waiter");
 }
        
        private void returnFromBreak(){
                
        		AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "done breaking");
                print("done breaking");
                host.msgDonewithBreak(this);
        }


        private void takecustomerOrder(myCustomer customer){
                
                customer.cusState = customerState.noAction;
                customer.c.msgWhatWouldYouLike(this);
                hostGui.DoGoToCustomer(customer.tablenumber);//animation
                hostGui.DoLeaveCustomer();

        }
        
          private void goHome() {
          Do("going home");
          isActive = false;
          mystate = WaiterState.unavailable;
          
  }
        
        public void setRequestingBreak(){
                if(requestBreak==false){
                        requestBreak=true;
                        stateChanged();
                        host.msgWaiterRequestBreak(this);
                }
        }


          private void needToReorder(myCustomer customer) {
                 
                    
                    customer.c.msgReorder();
                    customer.cusState = customerState.noAction;
                    stateChanged();
            }
          
          private void giveCheck(myCustomer customer){
                  BCheck thischeck=new BCheck(customer.choice);
                  cashier.msgCashierCheck(thischeck, customer.c);
                  customer.cusState=customerState.noAction;
                  stateChanged();
          }

        




        public void setToPause(){
                pauseChange();
                stateChanged();
        }

        private void pauseChange() {
                // TODO Auto-generated method stub
                
        }

        
        protected abstract void giveCookOrder(myCustomer customer);

                        /*print("Giving order to cook");
                        hostGui.DoLeaveCustomer();
                        customer.cusState = customerState.noAction;
                        cook.msgHereisanOrder(this, customer.choice, customer.tablenumber);*/


        

        private void giveFood(myCustomer customer){
                //hostGui.DoGoToPlating();
                //hostGui.DoGoToCustomer(customer.tablenumber);
                //hostGui.DoLeaveCustomer();
                customer.cusState = customerState.noAction;
                customer.c.msgHereisYourOrder(customer.choice);
        }

        // The animation DoXYZ() routines
        private void DoSeatCustomer(BCustomer customer, int tablenumber) {
                //Notice how we print "customer" directly. It's toString method will do it.
                //Same with "table"
        	AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "Seating " + customer + " at " + tablenumber);
                print("Seating " + customer + " at " + tablenumber);
                //hostGui.DoGoToWaiting();
                //hostGui.DoBringToTable(customer, tablenumber); 
                //hostGui.DoLeaveCustomer();
                

        }

        public void msgReadytoOrder(BCustomer customer){
                for (myCustomer thiscustomer : myCustomers){
                        if(thiscustomer.c==customer){
                                thiscustomer.cusState=customerState.readytoOrder;
                                stateChanged();
                        }
                }
        }


        //utilities

        public void setGui(BHostGui gui) {
                hostGui = gui;
        }
        
         public boolean requestBreak(){
                        return requestBreak;
            }
         
        public void setHost(BHostRole host){
                this.host = host;
    }
        
         public void setCashier(BCashier cashier){
                        this.cashier = cashier;
            }

        public BHostGui getGui() {
                return hostGui;
        }

        public class Table {
                BCustomer occupiedBy;
                int tableNumber;


                Table(int tableNumber) {
                        this.tableNumber = tableNumber;

                }




                
                void setOccupant(BCustomer cust) {
                        occupiedBy = cust;
                }

                  

                void setUnoccupied() {
                        occupiedBy = null;
                }

                BCustomer getOccupant() {
                        return occupiedBy;
                }

                boolean isOccupied() {
                        return occupiedBy != null;
                }

                public String toString() {
                        return "table " + tableNumber;
                }
        }

        

}
package simcity.BRestaurant;

import agent.Agent;
import simcity.BRestaurant.*;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.*;
import simcity.BRestaurant.gui.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Role;
import simcity.PersonAgent;
import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;
import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KWaiterSharedDataRole;
import simcity.KRestaurant.ProducerConsumerMonitor;
import simcity.TRestaurant.TCashierRole;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class BHostRole extends Role implements BHost, Host{
        static final int NTABLES = 3;//a global for the number of tables.
        //Notice that we implement waitingCustomers using ArrayList, but type it
        //with List semantics.
        public List<BCustomerRole> waitingCustomers
        =Collections.synchronizedList( new ArrayList<BCustomerRole>());

        public List<myWaiter> waiterList
        = Collections.synchronizedList(new ArrayList<myWaiter>());
        
        public List<myWaiter> breakWaiterList
        = Collections.synchronizedList(new ArrayList<myWaiter>());
        
        public boolean needBreak;
        public boolean returnToWork;
         private boolean isClosed;
         int hour;
         
        private BOrderStand theMonitor;
        private BCookRole mycook;
        private BCashierRole myCashier;
        
        private class myWaiter{
                public BWaiterRole w;
                public boolean needBreak=false;
                public boolean onBreak=false;
                public boolean returnToWork=false;

                public myWaiter(BWaiterRole waiter){
                        w=waiter;

                }


        }




        public Collection<Table> tables;
        //note that tables is typed with Collection semantics.
        //Later we will see how it is implemented

        private String name;
        private Semaphore atTable = new Semaphore(0,true);
        public int nextAvailableWaiter=0;
        
        public BHostGui hostGui = null;

        public BHostRole() {
                //super(p);

                this.name = name;
                // make some tables
                tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
                synchronized(tables){
                for (int ix = 1; ix <= NTABLES; ix++) {
                        tables.add(new Table(ix));//how you add to a collections
                        

                }
                }
                theMonitor = new BOrderStand();
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

        public void msgIWantFood(BCustomerRole cust) {
                
                waitingCustomers.add(cust);
                stateChanged();
        }
        
        public void msgDonewithBreak(BWaiterRole waiter){
                synchronized(waiterList) {
                for (myWaiter thisWaiter : waiterList){
                        if(thisWaiter.w==waiter){
                                thisWaiter.onBreak=false;
                                thisWaiter.returnToWork=true;
                        }
                }
                }
        }

        public void msgPutMeOnBreak(BWaiterRole waiter){
                synchronized(waiterList){
                for (myWaiter thisWaiter : waiterList){
                        if(thisWaiter.w==waiter){
                                thisWaiter.onBreak=true;
                        }
                }
                }
        }
        
        




        public void msgLeavingTable(BCustomerRole cust) {
                synchronized(tables){
                for (Table table : tables) {
                        if (table.getOccupant() == cust) {
                                print(cust + " leaving " + table);
                                table.setUnoccupied();
                                stateChanged();
                        }
                }
                }
        }

        public void msgAtTable() {//from animation
                //print("msgAtTable() called");
                atTable.release();// = true;
                stateChanged();
        }
        
         public void msgTimeUpdate(int hour){
         this.hour = hour;
         
 }
        
        public void msgWaiterRequestBreak(BWaiterRole waiter){
                synchronized(waiterList) {
                for (myWaiter thisWaiter : waiterList ){
                        if(thisWaiter.w==waiter){
                                thisWaiter.needBreak=true;
                    			AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Request break in host");
                                print("request break in host");
                                stateChanged();
                        }
                }
                }
        }
        
        public void msgIAmHere(Role r, String type){
                    
                     if(type.equals("waiter")){
                    	 AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Waiter is here");
                       Do("Waiter is here");
                       waiterList.add(new myWaiter((BWaiterRole) r));
                     }
                     else if(type.equals("cook")){
              			AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Cook is here");
                       Do("Cook is here");
                       mycook = (BCookRole)r;
                     }
                     else if(type.equals("cashier")){
              			AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Cashier is here");
                       Do("Cashier is here");
                       myCashier = (BCashierRole) r;
                     }
                     
                     else  if(!waiterList.isEmpty() && mycook != null && myCashier != null){
                       isClosed = false;
                     }
                     else{
                       isClosed = true;
                     }
                     
                     stateChanged();
                  }

        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
                
                  if(hour == 21 && !isClosed){
              closeRestaurant();
              return true;
      }
      
      if(hour == 20 || isClosed){
              restaurantClosed();
              return true;
      }
      
                synchronized(waiterList) {
                for (myWaiter thisWaiter : waiterList ){
                        if(thisWaiter.needBreak==true){
                                makeBreakDecision(thisWaiter);
                                
                                return true;
                        }
                }
                }
                synchronized(waiterList) {
                for (myWaiter thisWaiter : waiterList ){
                        if(thisWaiter.onBreak==true){
                                putOnBreak(thisWaiter);
                                
                                return true;
                        }
                }
                }
                synchronized(waiterList) {
                for (myWaiter thisWaiter : waiterList ){
                        if(thisWaiter.returnToWork==true){
                                takeOffBreak(thisWaiter);
                                
                                return true;
                        }
                }
                }
                synchronized(tables) {
                for (Table table : tables) {
                        if (!table.isOccupied()) {
                                int thistable=table.tableNumber;
                                
                                if (!waitingCustomers.isEmpty() && !waiterList.isEmpty()) {
                                        
                                        
                                        waiterList.get(nextAvailableWaiter).w.seatCustomer(waitingCustomers.get(0), thistable);
                                        table.setOccupant(waitingCustomers.get(0));
                                
                                        waitingCustomers.remove(waitingCustomers.get(0));
                                        nextAvailableWaiter = (nextAvailableWaiter+1)%waiterList.size();
                                        stateChanged();
                                }
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

        private void seatCustomer(BCustomerRole customer, Table table) {
                //customer.msgSitAtTable(table.tableNumber);
                DoSeatCustomer(customer, table);
                try {
                        atTable.acquire();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                table.setOccupant(customer);
                waitingCustomers.remove(customer);
                hostGui.DoLeaveCustomer();
        }
        
         private void closeRestaurant(){ //pay employees 50
  			AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Closing restaurant. It is "+hour);
         Do("Closing restaurant. It is "+hour);
         synchronized(waiterList){
                 for(myWaiter w: waiterList){
                        
                         w.w.msgGoHome(50);
                 }
         }
         myCashier.msgGoHome(50);
         mycook.msgGoHome(50);
         
         waiterList.clear();
         myCashier = null;
         mycook = null;
         isClosed = true;
 }
         
         private void restaurantClosed() {
   			AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Telling market is closed");
         Do("Telling market is closed");
        
                 waitingCustomers.clear();
         
 }
        
        private void makeBreakDecision(myWaiter waiter){
                int breakers=0;
                synchronized(waiterList) {
                for (myWaiter thiswaiter : waiterList){
                        if(thiswaiter.onBreak==true)
                                breakers++;
                }
                }
                if(waiterList.size()>1 && breakers<waiterList.size()-1){
                        waiter.w.msgBreakAllowed();
                        waiter.needBreak=false;
                        
                        print("break test");
                }
                
                else{
                        waiter.w.msgBreakNotAllowed();
                        waiter.needBreak=false;
                        
                }
        }
        
        private void putOnBreak(myWaiter waiter){
                synchronized(waiterList) {
                for(myWaiter w: waiterList){
                        if(w==waiter){
                                waiterList.remove(waiter);
                                breakWaiterList.add(waiter);
                                waiter.onBreak=false;
                                nextAvailableWaiter = (nextAvailableWaiter+1)%waiterList.size();
                        }
                }
                }
        }
        
        private void takeOffBreak(myWaiter waiter){
                synchronized(waiterList) {
                for(myWaiter w: waiterList){
                        if(w==waiter){
                                breakWaiterList.remove(waiter);
                                waiterList.add(waiter);
                                returnToWork=false;
                                nextAvailableWaiter = (nextAvailableWaiter+1)%waiterList.size();
                        }
                }
                }
        }
        
        private void tellWaitertoSeat(myWaiter waiter, BCustomerRole customer, Table table){
                
                waiter.w.msgSitCustomerAtTable(customer, table.tableNumber);
                table.setOccupant(customer);
                waitingCustomers.remove(customer);
                nextAvailableWaiter = (nextAvailableWaiter+1)%waiterList.size();
                stateChanged();
            }

        // The animation DoXYZ() routines
        private void DoSeatCustomer(BCustomerRole customer, Table table) {
                //Notice how we print "customer" directly. It's toString method will do it.
                //Same with "table"
   			AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BHost", "Seating " + customer + " at " + table);
                print("Seating " + customer + " at " + table);
                hostGui.DoBringToTable(customer, table.tableNumber); 

        }



        




        //utilities

        public void setGui(BHostGui gui) {
                hostGui = gui;
        }
        
        public void setCook(BCookRole c) {
                mycook = c;
                c.setMonitor(theMonitor);
        }

        public BHostGui getGui() {
                return hostGui;
        }
        
        public void setCashier(BCashierRole c) {
    		myCashier = c;
    	}

        public void setWaiter(BWaiterRole w){
                        
                
                        myWaiter thisWaiter=new myWaiter(w);
                        waiterList.add(thisWaiter);
                        if(w instanceof BWaiterSharedDataRole) {
                                ((BWaiterSharedDataRole) w).setMonitor(theMonitor);
                        }
                        stateChanged();
        }

        private class Table {
                BCustomerRole occupiedBy;
                int tableNumber;
                int xcoordinate;
                int ycoordinate;

                Table(int tableNumber) {
                        this.tableNumber = tableNumber;

                }







                void setOccupant(BCustomerRole cust) {
                        occupiedBy = cust;
                }

                void setUnoccupied() {
                        occupiedBy = null;
                }

                BCustomerRole getOccupant() {
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
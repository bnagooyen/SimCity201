package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
//import simcity.Transportation.CarAgent;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
//import simcity.Transportation.CarAgent;
import simcity.interfaces.Car;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
//import simcity.Market.gui.MCustomerGui;
import simcity.Market.MFoodOrder;
import simcity.Market.gui.MCustomerGui;
import agent.Role;

public class MarketCustomerRole extends Role implements MarketCustomer{

        public List<MFoodOrder> order = Collections.synchronizedList(new ArrayList<MFoodOrder>());
        private double myCheck;
        public int num;
        public enum customerState { talkToManager, timeToOrder, waiting, paying, done, storeClosed, pending }
        public customerState state;
        
        private MarketCashier mc;
        private MarketManager manager;
        public EventLog log;
        private PersonAgent p;
        
        private MCustomerGui customerGui;
        private Semaphore atCashier = new Semaphore(0, true);

        private SimCityGui gui;
        
        public MarketCustomerRole(SimCityGui gui, int instance) {
                super(); 
               // this.p = p;
                num=instance;
                this.gui = gui;
                log = new EventLog();
                state = customerState.talkToManager;
        }
        
        
        // messages
        public void msgAtCashier() {
        	atCashier.release();
        	stateChanged();
        }
        
        public void msgGoToCashier(MarketCashier c) {
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Told to go to cashier");
        		Do("Told to go to cashier");
                LoggedEvent e = new LoggedEvent("told to go to cashier");
                log.add(e);
                mc = c;
                state = customerState.timeToOrder;        
                stateChanged();
        }
        
        public void msgHereIsOrder(List<MFoodOrder> canGive) {
        	AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Got food and check");
    		Do("Got food and check");
            LoggedEvent e = new LoggedEvent("got food and check");
            log.add(e);
            
          
            updateMyFood(canGive);
            state = customerState.paying;
            stateChanged();
        }
    
        
        public void msgHereIsOrderAndCheck(List<MFoodOrder> canGive, double check) {
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Got food and check");
        		Do("Got food and check");
                LoggedEvent e = new LoggedEvent("got food and check");
                log.add(e);
                
              myCheck = calculateBill(canGive);
                updateMyFood(canGive);
                state = customerState.paying;
                stateChanged();
        }
        
        public void msgHereIsCarAndCheck(Car car, double check){
        	AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Got car and check");
        	Do("Got car and check");
        	
        	LoggedEvent e = new LoggedEvent("got car and check");
            log.add(e);
        	myCheck = check;
//        	myPerson.myCar = car;
        	state = customerState.paying;
            stateChanged();
        }

        public void msgMarketClosed() {
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Told market is closed");
        		Do("Told market is closed");
                LoggedEvent e = new LoggedEvent("told market is closed");
                log.add(e);
                state = customerState.storeClosed;
                stateChanged();
        }
        
        //scheduler
        public boolean pickAndExecuteAnAction() {
                if ( state == customerState.talkToManager) {
                        goToManager();
                        return true; 
                }
                if ( state == customerState.timeToOrder){ //&& purpose.equals("food")) {
                		Do("in");
                		orderFood();
                        return true;
                }
//                if( state == customerState.timeToOrder){// && purpose.equals("car")) {
//                    orderCar();
//                    return true;
//                }
                if ( state == customerState.paying ) {
                        payCheck();
                        return true;
                }
                if ( state == customerState.storeClosed ) {
                        leaveStore();
                        return true;
                }
                
                return false;
        }


        // actions
        private void goToManager() {
        		customerGui = new MCustomerGui(this);
        		customerGui.setPresent(true);
        		if(gui!=null) {
        			gui.myPanels.get("Market "+num).panel.addGui(customerGui);
        		}
    			AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Telling manager I'm here");
        		Do("Telling manager I'm here");
                LoggedEvent e = new LoggedEvent("telling manager I'm here");
                log.add(e);
                manager.msgIAmHere(this, "customer");
                state = customerState.waiting;

        }
        
        private void orderFood() {
        		customerGui.DoGoToCashier();
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Going to cashier");
        		Do("going to cashier");
        		try {
					atCashier.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Telling cashier my order");
        		Do("Telling cashier my order");
                state = customerState.waiting;
                LoggedEvent e = new LoggedEvent("telling cashier my order");
                log.add(e);
                
/************fix this message when figure out where person lives******************************************/
                mc.msgOrder(this, order, "");
        }
        
        private void orderCar(){
        	AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Telling cashier my car order");
        	Do("Telling cashier my car order");
        	state = customerState.waiting;
            LoggedEvent e = new LoggedEvent("telling cashier my car order");
            log.add(e);
            
           // **********fix this message when figure out where person lives******************************************/
            mc.msgCarOrder(this, "");
        }

        private void payCheck() {
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Paying check");
        		Do("Paying check");
                LoggedEvent e = new LoggedEvent("paying check");
                log.add(e);
                state = customerState.done;
//                myPerson.money -= myCheck;
                mc.msgHereIsPayment(this, myCheck);
                myCheck = 0;
                isActive = false;
                DoGoHome();

        }
        
        private void leaveStore() {
        		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCustomerRole", "Leaving market");
        		Do("Leaving market");
                LoggedEvent e = new LoggedEvent("leaving market");
                log.add(e);
                state = customerState.done;
                isActive = false;
                DoGoHome();
        }
        
        // animation
        private void DoGoHome() {
        	customerGui.DoGoHome();
                
        }

        // utilities
        private void updateMyFood(List<MFoodOrder> canGive) {
        // TODO Auto-generated method stub
        
        }
        
        private double calculateBill(List<MFoodOrder> canGive) {
                double myCalculation = 0;
                for(MFoodOrder o : canGive) {
                        myCalculation += o.amount * o.price;
                }
                return myCalculation;
        }

        public void setMarketManager(MarketManager m) {
                manager = m;
        }
        
        public double getMyCheck() {
                return myCheck;
        }
        public void setGui(MCustomerGui g) {
        	customerGui = g;
        }
        public void populateOrderList(String type, int amount) {
        	order.add(new MFoodOrder(type, amount));
        }
        
}
/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.DRestaurant;

//import simcity.BRestaurant.BFood;
import simcity.DRestaurant.DCookRole.InventoryOrder.InventoryOrderState;
import simcity.DRestaurant.DOrder.OrderState;
import agent.Role;
import simcity.Market.MFoodOrder;
import simcity.gui.SimCityGui;
import simcity.DRestaurant.DGui.DCookGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Cook;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
//import simcity.DCookRole.InventoryOrder.InventoryOrderState;

import simcity.interfaces.RestaurantCashier;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cashier Role
 */

public class DCookRole extends Role implements DCook, Cook{

        private DProducerConsumerMonitor theMonitor;
        SimCityGui gui;
        Timer timer = new Timer();
        private String name;
        private Map<String, DFood> myFood = new HashMap<String, DFood>();
        //public HostGui hostGui = null;
        private int initialFoodAmnt= 4;
        private static final int MAXCAPACITY=10;
        private int threshold = 4;
        //private final int NUM_MARKETS = 3;
    
        public DCookGui CookGui = null;
        
        private Semaphore atFridge = new Semaphore(0,true);
        private Semaphore atGrill = new Semaphore(0, true);
        private Semaphore atPlating = new Semaphore(0, true);
        private Semaphore atKitchen = new Semaphore(0, true);
        private Semaphore atTheDoor = new Semaphore(0, true);
        
        //boolean justArrived;
        DCashier myCashier;
        private DWaiterRole waiter;
        private DHostRole host;

        private Map<String, Double> prices = new HashMap<String, Double>();
        private boolean waitingForInventory; // order at once, don't order when waiting for an order to arrive
        //private boolean reOrdering;
        
        Map<String, Boolean> grillOccupied = new HashMap<String, Boolean>();
        
        ArrayList<MyDelivery> delivery= new ArrayList<MyDelivery>();
        List<DOrder> orders =  Collections.synchronizedList(new ArrayList<DOrder>());
        ArrayList<MarketManager> myMarkets = new ArrayList<MarketManager>();
        //ArrayList<DMarketAgent> markets = new ArrayList<DMarketAgent>();
        List<InventoryOrder> myOrders =  Collections.synchronizedList(new ArrayList<InventoryOrder>());
        private int ORDER_ID;
        ArrayList<MFoodOrder> orderToMarket = new ArrayList<MFoodOrder>();
        //private int marketToSendOrdersTo;
        enum CookState {justArrived, atKitchen, offDuty, onDuty};
        CookState state;
        //boolean goingToKitchen=false, inKitchen =false;
        boolean RestaurantIsOpen, CheckedAtFirst;// valsAreSet;
        
        public DCookRole(SimCityGui gui) {
                super();
                this.gui=gui;
                
                //fill up kitchen with food
                myFood.put("Chicken", new DFood("Chicken", 5, initialFoodAmnt));
                myFood.put("Steak", new DFood("Steak", 10, initialFoodAmnt));
                myFood.put("Salad", new DFood("Salad", 3, initialFoodAmnt));
                myFood.put("Pizza", new DFood("Pizza", 6, initialFoodAmnt));
     
                grillOccupied.put("Pizza", false);
                grillOccupied.put("Steak", false);
                grillOccupied.put("Chicken", false);
                grillOccupied.put("Salad", false);
                
                prices.put("Steak", 10.0);
                prices.put("Chicken", 7.0);
                prices.put("Salad", 3.0);
                prices.put("Pizza", 5.0);
                
                waitingForInventory=false;

                
                ORDER_ID=1;
                state=CookState.justArrived;
                RestaurantIsOpen=false; CheckedAtFirst=false; 
    
        }


        // The animation DoXYZ() routines
        private void DoGoToKitchen() {
        	CookGui.DoGoToKitchen();
        }
        private void DoLeaveRestaurant() {
        	CookGui.DoLeaveRestaurant();
        }
        private void DoClearPlating(String foo) {
                DCookGui.DoClearPlating(foo);
        }
        
        private void DoGoToRefrigerator() {
                CookGui.DoGoToRefrigerator();
        }
        
        private void DoGoToPlating() {
                CookGui.DoGoToPlating();
        }
        private void DoGoToPizzaGrill() {
                CookGui.DoGoToPizzaGrill();
        }
        private void DoGoToSteakGrill() {
                CookGui.DoGoToSteakGrill();                
        }
        private void DoGoToChickenGrill() {
                CookGui.DoGoToChickenGrill();        
        }
        private void DoGoToSaladGrill() {
                CookGui.DoGoToSaladGrill();        
        }

        private void DoGoToPizzaGrill2() {
                CookGui.DoGoToPizzaGrill2();
        }
        private void DoGoToSteakGrill2() {
                CookGui.DoGoToSteakGrill2();                
        }
        private void DoGoToChickenGrill2() {
                CookGui.DoGoToChickenGrill2();        
        }
        private void DoGoToSaladGrill2() {
                CookGui.DoGoToSaladGrill2();        
        }
        

        @Override
        public String getName() {
                return name;
        }

        // Messages

        //hack!
        
        @Override
		public void msgSetInventory(int val) {
			// TODO Auto-generated method stub
        	Iterator it = myFood.entrySet().iterator();
    		
    		while(it.hasNext()){
    			Map.Entry obj = (Map.Entry)it.next();
    			DFood f = (DFood) obj.getValue();
    			f.setAmount(val);
    		}
    		
		}
        
        public void AddHost(DHostRole h) {
                //System.out.println("host added to cook");
                host=h;
        }
        
        public void AddCashier(DCashier h) {
                //System.out.println("host added to cook");
                myCashier=h;
        }
        
        
        public void msgAddWaiter(DWaiterRole w) {
                waiter=w;
        }
        
        public void msgAddMarket(MarketManager m) {
                myMarkets.add(m);
        }
        
        public void msgIncKitchenThreshold() {
                threshold++;
                System.out.println("Kitchen threshold increased to "+ threshold);
        }
        public void msgDecKitchenThreshold() {
                if(threshold>1) {
                        threshold--;
                        System.out.println("Kitchen threshold decresed to "+ threshold);
                }
                else {
                        System.out.println("Cannot drop Kitchen threshold below 1");
                }
        }
        
        public void msgIncKitchenAmnt() {
                if(initialFoodAmnt<MAXCAPACITY) {
                initialFoodAmnt++;
                //fill up kitchen with food
                        myFood.put("Chicken", new DFood("Chicken", 5, initialFoodAmnt));
                        myFood.put("Steak", new DFood("Steak", 10, initialFoodAmnt));
                        myFood.put("Salad", new DFood("Salad", 3, initialFoodAmnt));
                        myFood.put("Pizza", new DFood("Pizza", 6, initialFoodAmnt));
                        
                        AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Cook kitchen inventory updated");
                        System.out.println("Cook Kitchen Inventory is updated to "+ initialFoodAmnt);
                }
                else {
                		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Kitchen filled to capacity");
                        System.out.println("kitchen filled to capacity!");
                }
        }
        
        public void msgDecKitchenAmnt() {
                if(initialFoodAmnt>0) {
                        initialFoodAmnt--;
                        //fill up kitchen with food
                        myFood.put("Chicken", new DFood("Chicken", 5, initialFoodAmnt));
                        myFood.put("Steak", new DFood("Steak", 10, initialFoodAmnt));
                        myFood.put("Salad", new DFood("Salad", 3, initialFoodAmnt));
                        myFood.put("Pizza", new DFood("Pizza", 6, initialFoodAmnt));

                        System.out.println("Cook Kitchen Inventory is updated to "+ initialFoodAmnt);
                }
                else {
                        System.out.println("Kitchen Inventory can not be lower than 0");
                }
        }
        
        /********help me please!!!*********/

        public void msgOffDuty(double money){
        	myPerson.money+=money;
        	state=CookState.offDuty;
        	stateChanged();
        }
        @Override
        public void msgHereIsDelivery(List<MFoodOrder> canGive, double check,
                        MarketManager manager, MarketCashier mc) {
                // TODO Auto-generated method stub
        		MyDelivery dlv = new MyDelivery(canGive, check, mc, manager);
        		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Received delivery");
        		System.out.println("received delivery with bill = "+ dlv.check);
                delivery.add(dlv);
                stateChanged();
        }

        public void msgOnDuty() {
        	state=CookState.onDuty;
        	stateChanged();
        }

        @Override
        public void msgGoToCashier(MarketCashier c) {
                // TODO Auto-generated method stub
                
        }


        @Override
        public void msgMarketClosed() {
                // TODO Auto-generated method stub
                
        }
        
        
//        public void msgCheckInventoryValsForOpen() {
//                //System.out.println("received msg");
//                valsAreSet=true;
//                stateChanged();
//        }
        
        public void msgTimeToCheckStand() {
                stateChanged();
        }
        
//        public void msgClearPlatingForOrder(String order) {
//                System.err.println("received clear plating");
//                for(Order ord: orders) {
//                        if(ord.tablenum==o.tablenum) {
//                                ord.state=OrderState.clearPlating;
//                                stateChanged();
//                        }
//                }
//        }
        
        public void msgShouldIPayThisBill(double amt, MarketManager ma) {
        		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Approving bill to pay");
                System.out.println("cashier received shouldipaythisbill for "+amt);
                for(InventoryOrder o: myOrders){
                        if(o.market==ma) {
                                if(Math.abs(o.billExpected-amt)<=0.01)
                                        o.state=InventoryOrderState.approved;
                                else 
                                        o.state=InventoryOrderState.denied;        
                                stateChanged();
                                break;
                        }
                }
        }
        
        public void msgHereIsAnOrder(DOrder order) {
                orders.add(new DOrder(order.getChoice(), order.getTablenum(), order.getWaiter()));
                AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Received order");
                System.out.println("cook received order of "+ order.getChoice()+ " from table "+order.tablenum);
                stateChanged();
        }
        

        public void msgAnimationLeftRestaurant() {
        	atTheDoor.release();
        	stateChanged();
        }
        public void msgAnimationArrivedAtFridge() {
                atFridge.release();
                stateChanged();
        }
        
        public void msgAnimationArrivedAtGrill() {
                atGrill.release();
                stateChanged();
        }
        
        public void msgAnimationArrivedAtPlating() {
                atPlating.release();
                stateChanged();
        }
        
        //utilities

        public void setGui(DCookGui gui) {
                CookGui = gui;
        }
        
        public void setMonitor(DProducerConsumerMonitor m) {
                theMonitor= m;
        }
        
        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        @Override
        public boolean pickAndExecuteAnAction() {

        	
        	if(state==CookState.justArrived) {
        		tellHost();
        		return true;
        	}
        	
        	if(state==CookState.onDuty) {
        		goToKitchen();
        		return true;
        	}
        	
        	if(state==CookState.offDuty) {
        		leaveRestaurant();
        		return true;
        	}
                synchronized(orders) {
                for(int i=0; i<orders.size(); i++) {
                        if(orders.get(i).state==OrderState.clearPlating) {
                                ClearPlating(orders.get(i));
                                return true;
                        }
                }
                }
                
                synchronized(myOrders) {
                    for(int i=0; i<myOrders.size(); i++) {
                            if(myOrders.get(i).state==InventoryOrderState.approved || myOrders.get(i).state==InventoryOrderState.denied) {
                                    NotifyCashierOfVerification(myOrders.get(i));
                                    return true;
                            }
                    }
                    }
                
                synchronized(myOrders) {
                for(int i=0; i<myOrders.size(); i++) {
                        if(myOrders.get(i).state==InventoryOrderState.needsReorder) {
                                ReorderFood(myOrders.get(i));
                                return true;
                        }
                }
                }
                
                if(!delivery.isEmpty()) {
                        ProcessDelivery(delivery.get(0));
                        return true;
                }
                if(!waitingForInventory) {
                        if((myFood.get("Chicken").getAmount()<=threshold || myFood.get("Pizza").getAmount()<=threshold ||
                                        myFood.get("Steak").getAmount()<=threshold || myFood.get("Salad").getAmount()<=threshold)) {
                                                OrderFoodFromMarket();
                                                return true;
                                        }
                }
   
        
                for(int i=0; i<orders.size(); i++) {
                        if(orders.get(i).state==OrderState.grillInUse && !grillOccupied.get(orders.get(i).getChoice())) {
                                CookOrder(orders.get(i));
                                return true;
                        }
                }
                
                for(int i=0; i<orders.size(); i++) {
                        if(orders.get(i).state==OrderState.pending) {
                                CookOrder(orders.get(i));
                                return true;
                        }
                }
                
                for(int i=0; i<orders.size(); i++) {
                        if(orders.get(i).state==OrderState.cooked) {
                                PlateOrderAndCallWaiter(orders.get(i));
                                return true;
                        }
                }
                

                
                
                if(!RestaurantIsOpen && !waitingForInventory && state==CookState.atKitchen) {
                        CheckIfFullyStocked(); 
                        return true;
                }
                
                if(state==CookState.atKitchen) {
                CheckRotatingStand();
                }
                
                return false;
                //we have tried all our rules and found
                //nothing to do. So return false to main loop of abstract agent
                //and wait.
        }


        // Actions
        		private void tellHost() {
        			if(CookGui==null) {
        				CookGui = new DCookGui(this);
        				gui.myPanels.get("Restaurant 3").panel.addGui(CookGui);
        				host.msgIAmHere(this, "cook");
        			}
        		}
        		
        		private void goToKitchen() {
        			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "On duty. Going to kitchen");
        			Do("On duty.. going to kitchen");
        			DoGoToKitchen();
        			state=CookState.atKitchen;
        		}
        		private void leaveRestaurant() {
        			DoLeaveRestaurant();
        			try {
						atTheDoor.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			state=CookState.justArrived;
        			isActive=false;
        		}
                private void CheckRotatingStand() {
                        DOrder o = theMonitor.remove();
                        if(o!=null) {
                        		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Received order from revolving table");
                                System.out.println("Order Received from Revolving Table");
                                DOrder newOrder = new DOrder(o.getChoice(), o.getTablenum(), o.getWaiter());
                                orders.add(newOrder);
                        }
                        else {
                                timer.schedule(new TimerTask() {
                                        @Override
										public void run() {
                                                msgTimeToCheckStand();
                                        }
                                }, 2000);
                        }
                }
        
                private void NotifyCashierOfVerification(InventoryOrder o) {
                		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Notifying cashier");
                        System.out.println("notifying cashier");
                        if(o.state==InventoryOrderState.approved) {
                                myCashier.msgAnswerVerificationRequest(true);
                        }
                        if(o.state==InventoryOrderState.denied) {
                                myCashier.msgAnswerVerificationRequest(false);
                        }
                        myOrders.remove(o);
                }
                private void ClearPlating(DOrder o) {
                        DoClearPlating(o.getChoice().substring(0,2));
                        orders.remove(o);
                }
                private void CheckIfFullyStocked() {
                        if(myFood.get("Chicken").getAmount()>=threshold && myFood.get("Steak").getAmount()>=threshold &&
                                        myFood.get("Pizza").getAmount()>=threshold && myFood.get("Salad").getAmount()>=threshold) {
                                host.msgKitchenIsReady();
                                RestaurantIsOpen=true;
                        }
                }
                
                private void CookOrder(final DOrder o) {
//                        
                        DFood food=myFood.get(o.getChoice());
                        
                        if(food.getAmount()==0) {
                                        System.out.println("Out of "+ food.getChoice());
                                        o.getWaiter().msgOutOfFood(o);
                                        orders.remove(o);
                                        return;
                                }
                        
                        food.setAmount(food.getAmount()-1);
                        myFood.put(o.getChoice(), food);
                        
                        if(grillOccupied.get(o.getChoice())) {
                                o.state=OrderState.grillInUse;
                                return;
                        }
                        
                        grillOccupied.put(o.getChoice(), true);
                        DoGoToRefrigerator();
                        
                        try {
                                atFridge.acquire();
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        
                        //System.err.println("made to fridge... "+ o.getChoice());
                        
                        if(o.getChoice().equals("Pizza")) {
                                DoGoToPizzaGrill();
                        }
                        else if(o.getChoice().equals("Steak")) {
                                DoGoToSteakGrill();
                        }
                        else if(o.getChoice().equals("Chicken")) {
                                DoGoToChickenGrill();
                        }
                        else if(o.getChoice().equals("Salad")) {
                                DoGoToSaladGrill();
                        }
                        
                        
                        try {
                                atGrill.acquire();
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        
//                        System.err.println("made to grill");
                        
                        o.state=OrderState.cooking;
                        
                        timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                        print(o+" is cooked!");
                                        o.state=OrderState.cooked;
                                        //isHungry = false;
                                        stateChanged();
                                }
                        },
                        myFood.get(o.getChoice()).getCookTime()*1000);
                        
                }


                private void PlateOrderAndCallWaiter(DOrder o) {
                        
                        o.state=OrderState.plated;

                        
                        
                        if(o.getChoice().equals("Pizza"))
                                DoGoToPizzaGrill2();
                        else if(o.getChoice().equals("Steak"))
                                DoGoToSteakGrill2();
                        else if(o.getChoice().equals("Chicken"))
                                DoGoToChickenGrill2();
                        else if(o.getChoice().equals("Salad"))
                                DoGoToSaladGrill2();
                        
                        try {
                                atGrill.acquire();
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        
                        grillOccupied.put(o.getChoice(), false);
//                        
//                        System.out.println("at Grill!");
                        
                        DoGoToPlating();
                        
                        try {
                                atPlating.acquire();
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        
                        System.out.println("order plated");
                        o.getWaiter().msgOrderIsReady(o.getTablenum());
                        orders.remove(o);
                }
                
//                private void CallWaiter(Order o) {
//                        o.getWaiter().msgOrderIsReady(o);
//                        orders.remove(o);
//                }
                
                private void OrderFoodFromMarket() {

                		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Need to order");
                        System.out.println("need to order");
                        double billAmnt = 0;
                        orderToMarket.clear(); //restart a new order
                        
                        if(myFood.get("Chicken").getAmount()<=threshold) {
                                orderToMarket.add(new MFoodOrder("Chicken", MAXCAPACITY- myFood.get("Chicken").getAmount()));
                                billAmnt+=(MAXCAPACITY - myFood.get("Chicken").getAmount())*prices.get("Chicken");
                        }
                        
                        if(myFood.get("Steak").getAmount()<=threshold) {
                                orderToMarket.add(new MFoodOrder("Steak", MAXCAPACITY-myFood.get("Steak").getAmount()));
                                //System.err.println(MAXCAPACITY - myFood.get("Steak").getAmount());
                                billAmnt+=(MAXCAPACITY - myFood.get("Steak").getAmount())*prices.get("Steak");
                        }
                        
                        if(myFood.get("Pizza").getAmount()<=threshold) {
                                orderToMarket.add(new MFoodOrder("Pizza", MAXCAPACITY-myFood.get("Pizza").getAmount()));
                                billAmnt+=(MAXCAPACITY - myFood.get("Pizza").getAmount())*prices.get("Pizza");
                        }
                        
                        if(myFood.get("Salad").getAmount()<=threshold) {
                                orderToMarket.add(new MFoodOrder("Salad", MAXCAPACITY-myFood.get("Salad").getAmount()));
                                billAmnt+=(MAXCAPACITY - myFood.get("Salad").getAmount())*prices.get("Salad");
                        }

                        myMarkets.get(0).msgIAmHere(this, orderToMarket, "DRestaurant", "cook", (RestaurantCashier)myCashier);
                        
                       myOrders.add(new InventoryOrder(myMarkets.get(0), 1, billAmnt, ORDER_ID));
                       System.out.println("expecting bill for "+ billAmnt);
                        ORDER_ID++;
                        waitingForInventory=true;
                }
                
                private void ReorderFood(InventoryOrder reord) {
                        DecimalFormat df = new DecimalFormat("###.##");
                        if(reord.getMarketOrderingFrom()>myMarkets.size()) {
                                for(int i=0; i< reord.myorder.size(); i++) {
                                        System.out.println("all markets are out of "+ reord.myorder.get(i).type);
                                }
                                if(!RestaurantIsOpen && reord.getMarketOrderingFrom()>myMarkets.size()) { //markets don't have it.. should just open anyway
                                        RestaurantIsOpen=true;
                                        host.msgKitchenIsReady();
                                }
                                myOrders.remove(reord);
                                return;
                        }
                        
                        double billAmnt =0;
                        for(MFoodOrder ord: reord.myorder) {
                                if(ord.type.equals("Chicken")) {
                                        billAmnt+=ord.amount*prices.get("Chicken");
                                }
                                if(ord.type.equals("Steak")) {
                                        billAmnt+=ord.amount*prices.get("Steak");
                                }
                                if(ord.type.equals("Salad")) {
                                        billAmnt+=ord.amount*prices.get("Salad");
                                }
                                if(ord.type.equals("Pizza")) {
                                        billAmnt+=ord.amount*prices.get("Pizza");
                                }
                        }
                        
                        
                        AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCookRole", "Sent a reorder");
                        System.out.println("Cook sent reorder");
                        myMarkets.get(reord.mktOrderingFrom-1).msgIAmHere(this, orderToMarket, "DRestaurant", "cook", (RestaurantCashier)myCashier);
                        reord.billExpected=billAmnt;
                        for(InventoryOrder ord: myOrders) {
                                if(ord.state==InventoryOrderState.needsPriceUpdate) {
                                        ord.billExpected=Double.parseDouble(df.format(ord.billExpected-billAmnt));
                                        ord.state=InventoryOrderState.ordered;
                                }
                        }
                        
                        waitingForInventory=true;
                        reord.state=InventoryOrderState.ordered;
                
                        
                        
                }
                
        
                private void ProcessDelivery(MyDelivery d) {
                        for(MFoodOrder foo: d.delivery) {
                                DFood temp = myFood.get(foo.type);
                                temp.setAmount(temp.getAmount()+foo.amount);
                                myFood.put(foo.type, temp);
                                
                        }
                        waitingForInventory=false;
        
                }


                class MyDelivery {
                        public double check;
                        public MarketCashier mcashier;
                        public MarketManager mmanager;
                        public List<MFoodOrder> delivery;
                        public MyDelivery(List<MFoodOrder> dlv, double check, MarketCashier c, MarketManager m) {
                                delivery=new ArrayList<MFoodOrder>();
                                delivery=dlv;
                                this.check = check;
                                mcashier=c;
                                mmanager=m;
                        }
                }
                
                static class InventoryOrder {
                        int orderID;
                        ArrayList<MFoodOrder> myorder;
                        int mktOrderingFrom;
                        MarketManager market;
                        double billExpected;
                        enum InventoryOrderState {ordered, needsReorder, approved, denied, needsPriceUpdate};
                        InventoryOrderState state;
                        InventoryOrder (ArrayList<MFoodOrder> mo, int orderid) {
                                myorder = new ArrayList<MFoodOrder>();
                                myorder=mo;
                                mktOrderingFrom=1;
                                orderID=orderid;
                        }
                                                
                        InventoryOrder(MarketManager mkt, int mknum, double billAmt, int ord) {
                                market= mkt;
                                billExpected=billAmt;
                                //sent=true;
                                orderID = ord;
                                mktOrderingFrom=mknum;
                        }
                        
                        MarketManager getMarket() {
                                return market;
                        }
                        int getMarketOrderingFrom() {
                                return mktOrderingFrom;
                        }
                        
                        void incMarketOrderingFrom(){
                                mktOrderingFrom++;
                        }
                        
        
                }



}
package simcity.LRestaurant;

//import restaurant.gui.RestaurantGui;
import agent.Role;
import simcity.LRestaurant.gui.LCustomerGui;
import simcity.LRestaurant.interfaces.LCashier;
import simcity.LRestaurant.interfaces.LCustomer;
import simcity.LRestaurant.interfaces.LWaiter;
import simcity.LRestaurant.LMenu;
import simcity.PersonAgent;
import simcity.LRestaurant.LMenu;
import simcity.LRestaurant.interfaces.LCustomer;
import simcity.LRestaurant.interfaces.LWaiter;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Customer Agent
 */
public class LCustomerRole extends Role implements LCustomer{
        private String[] name;
        private int hungerLevel = 5;        // determines length of meal
        private int table;
        private int money;
        private int check;
        private int needToPay;
        private boolean canOrder;
        Timer timer = new Timer();
        Random rand = new Random();
        public LCustomerGui customerGui;
        private String choice; //place holder
        private LMenu m;
        private boolean willStay;
        private String givenName;

        // agent correspondents
        private LWaiter waiter;
        private LHostRole host;
        private LCashier cashier;
        //private WaiterAgent waiter;

        //    private boolean isHungry = false; //hack for gui
        public enum AgentState
        {DoingNothing, WaitingInRestaurant, DecidingToLeave, BeingSeated, Seated, ToldOrder, Ordered, OrderAgain, WaitingForFood, Eating, gettingCheck, waitingCheck, receiveCheck, noOrder, Leaving};
        private AgentState state = AgentState.DoingNothing;//The start state

        public enum AgentEvent 
        {none, shouldIStay, standby, gotHungry, followWaiter, reordering, seated, deciding, tellingOrder, orderFood, gotFood, doneEating, askingCheck, gettingWallet, askedForCheck, paying, gotChange, noMoney, doneLeaving};
        AgentEvent event = AgentEvent.none;

        /**
         * Constructor for CustomerAgent class
         *
         * @param name name of the customer
         * @param gui  reference to the customergui so the customer can send it messages
         */
        public LCustomerRole(PersonAgent p){
                super(p);
                needToPay = 0;
                givenName = p.getName();
                name = givenName.split("-"); //name-money-choice-behavior

                m = new LMenu();

                if(name.length != 4){ //cases just in case user fails to input everything
                        name = new String[] {"Default","20","S","wait"};
                }

                if(isNumeric(name[1])){
                        money = Integer.parseInt(name[1]);
                }
                else{
                        //name = new String[] {"Default","20","S","wait"};
                		money = 20;
                }
                
                        
                //use name input as the choice
                if(name[2].equals("S") || name[2].equals("Ch")  || name[2].equals("St")  || name[2].equals("P") ){
                        choice = name[2];
                }
                else{
                        //name = new String[] {"Default","20","S","wait"};
                		choice = "S";
                }

                if(name[3].equals("leave")){
                        willStay = false;
                }
                else{
                        willStay = true;
                }
        }

        private boolean isNumeric(String string) {
                try{
                        @SuppressWarnings("unused")
                        int num = Integer.parseInt(string);
                }
                catch(NumberFormatException error){
                        return false;
                }
                return true;
        }

        /**
         * hack to establish connection to Host agent.
         */

        public void setHost(LHostRole host) {
                this.host = host;
        }

        public void setWaiter(LWaiter waiter) {
                this.waiter = waiter;
        }

        public void setCashier(LCashier cashier){
                this.cashier = cashier;
        }

        public String getCustomerName() {
                return name[0];
        }
        // Messages

        public void gotHungry() {//from animation
                print("I'm hungry");
                event = AgentEvent.gotHungry;
                stateChanged();
        }

        public void msgRestaurantIsFull(){
                event = AgentEvent.shouldIStay;
                stateChanged();
        }

        public void msgFollowMe(int tableNumber, LMenu m, LWaiter w) {
                this.waiter = w;
                this.m = m;
                table = tableNumber;
                print("Received msgSitAtTable");
                event = AgentEvent.followWaiter;
                //canOrder = m.affordable(money);
                stateChanged();
        }

        public void msgPleaseReorder(LMenu m) {
                this.m = m;
                event = AgentEvent.reordering;
                stateChanged();
        }

        public void msgWhatWouldYouLike() {
                //print("Received msgSitAtTable");
                event = AgentEvent.orderFood;
                stateChanged();
        }

        public void msgHereIsFood() {
                //print("Received msgSitAtTable");
                event = AgentEvent.gotFood;
                stateChanged();
        }

        public void msgHereIsCheck(int check){
                Do("Received check from waiter");
                this.check = check;
                event = AgentEvent.gettingWallet;
                stateChanged();
        }

        public void msgHereIsChange(int cash){
                if(cash != -1){needToPay = 0; money = cash;}
                
                event = AgentEvent.gotChange;
                stateChanged();
        }

        public void msgAnimationFinishedGoToSeat() {
                //from animation
                event = AgentEvent.seated;
                stateChanged();
        }
        public void msgAnimationFinishedLeaveRestaurant() {
                //from animation
                event = AgentEvent.doneLeaving;
                stateChanged();
        }

        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
                //        CustomerAgent is a finite state machine

                if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
                        state = AgentState.WaitingInRestaurant;
                        goToRestaurant();
                        return true;
                }
                if (state == AgentState.WaitingInRestaurant&& event == AgentEvent.shouldIStay ){
                        state = AgentState.DecidingToLeave;
                        decideIfStay();
                        return true;
                }
                if (state == AgentState.WaitingInRestaurant && event.equals(AgentEvent.followWaiter)){
                        state = AgentState.BeingSeated;
                        SitDownTable(table);
                        return true;
                }
                if (state == AgentState.BeingSeated && event == AgentEvent.seated){
                        state = AgentState.Seated;
                        DecidingOrder();
                        return true;
                }
                if (state == AgentState.Seated && event == AgentEvent.deciding){
                        state = AgentState.Ordered;
                        ReadyToOrder();
                        return true;
                }
                if (state == AgentState.Ordered && event == AgentEvent.orderFood){
                        state = AgentState.ToldOrder;
                        TellingOrder();
                        return true;
                }
                if (state == AgentState.ToldOrder && event == AgentEvent.tellingOrder){
                        state = AgentState.WaitingForFood;
                        GiveOrder();
                        return true;
                }
                if (state == AgentState.WaitingForFood && event.equals(AgentEvent.reordering)){
                        state = AgentState.OrderAgain;
                        DecidingOrder();
                        return true;
                }
                if (state == AgentState.OrderAgain && event == AgentEvent.deciding){
                        state = AgentState.Ordered;
                        ReadyToOrder();
                        return true;
                }
                if (state == AgentState.WaitingForFood && event == AgentEvent.gotFood){
                        state = AgentState.Eating;
                        EatFood();
                        return true;
                }
                if (state == AgentState.Eating && event == AgentEvent.doneEating){
                        state = AgentState.gettingCheck;
                        askForCheck();
                        return true;
                }
                if (state == AgentState.gettingCheck && event == AgentEvent.gettingWallet){
                        state = AgentState.receiveCheck;
                        payCheck();
                        return true;
                }
                if (state == AgentState.noOrder && event == AgentEvent.noMoney){
                        state = AgentState.Leaving;
                        leaveTable();
                        return true;
                }
                if (state == AgentState.receiveCheck && event == AgentEvent.gotChange){
                        state = AgentState.Leaving;
                        leaveTable();
                        return true;
                }
                if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
                        state = AgentState.DoingNothing;
                        //no action
                        return true;
                }
                return false;
        }

        // Actions

        private void goToRestaurant() {
                Do("Going to restaurant");
                customerGui.DoWait();
                host.msgIWantToEat(this);//send our instance, so he can respond to us
        }

        private void decideIfStay(){


                if(!willStay){
                        Do("Too full, deciding to leave.");
                        host.msgChoseToLeave(this);
                        customerGui.DoExitRestaurant();
                        state = AgentState.DoingNothing;
                }
                else{
                        Do("Too full, but staying.");
                        host.msgChoseToWait(this);
                        state = AgentState.WaitingInRestaurant;
                        event = AgentEvent.standby;
                }

                stateChanged();
        }

        private void SitDownTable(int tableNum) {
                Do("Being seated. Going to table");
                customerGui.DoGoToSeat(tableNum);
//                host.msgLeftLine();
        }

        private void DecidingOrder() {
                customerGui.deciding();

                boolean stillOrdering = true;
                canOrder = m.affordable(money);

                if(!canOrder){ //deciding if customer will still order even though they don't have money
                        if(!willStay){
                                stillOrdering = false;
                        }
                        else{ //name.equals("poor")
                                stillOrdering = true;
                        }
                }

                if(stillOrdering){
                        timer.schedule(new TimerTask() {
                                public void run() {
                                        event = AgentEvent.deciding;
                                        stateChanged();
                                }
                        },
                        1000);

                        customerGui.setFood(choice);
                        Do("Deciding order " + choice);
                }
                else{
                        timer.schedule(new TimerTask() {
                        public void run() {
                                        Do("Cannot afford. Leaving.");
                                        state = AgentState.noOrder;
                                        event = AgentEvent.noMoney; //leaving table
                                        stateChanged();
                                }
                        },
                        1000);
                }
        }

        private void ReadyToOrder() {
                print("Ready to Order.");
                waiter.msgReadyToOrder(this);
        }

        private void TellingOrder() {
                customerGui.doneDeciding();
                Do("Telling order");

                timer.schedule(new TimerTask() {
                        public void run() {
                                event = AgentEvent.tellingOrder;
                                //isHungry = false;
                                stateChanged();
                        }
                },
                700);

        }

        private void GiveOrder() {
                Do("Ordering");
                waiter.msgHereIsMyChoice(this,choice);
        }


        private void EatFood() {
                customerGui.gotFood();

                Do("Eating Food");
                //This next complicated line creates and starts a timer thread.
                //We schedule a deadline of getHungerLevel()*1000 milliseconds.
                //When that time elapses, it will call back to the run routine
                //located in the anonymous class created right there inline:
                //TimerTask is an interface that we implement right there inline.
                //Since Java does not all us to pass functions, only objects.
                //So, we use Java syntactic mechanism to create an
                //anonymous inner class that has the public method run() in it.
                timer.schedule(new TimerTask() {
                        public void run() {
                                print("Done eating, " + choice);
                                event = AgentEvent.doneEating;
                                //isHungry = false;
                                stateChanged();
                        }
                },
                5000);//getHungerLevel() * 1000);//how long to wait before running task
        }

        private void askForCheck(){
                Do("Asking waiter for check");
                waiter.msgReadyForCheck(this);
                event = AgentEvent.askedForCheck;
                stateChanged();
        }

        private void payCheck(){


                needToPay +=check;

                Do("needToPay = "+needToPay);
                Do("Money = "+money);
                Do("Leaving and heading to cashier");
                cashier.msgHereIsPayment(this,money,needToPay);
                event = AgentEvent.paying;
                stateChanged();
        }

        private void leaveTable() {
                customerGui.ateFood();
                customerGui.DoExitRestaurant();

                Do("Left the restaurant");
                waiter.msgDoneEatingAndLeaving(this);
                if(money == 0){
                        goToBank();
                }
        }

        private void goToBank(){
                money += 30;
        }

        // Accessors, etc.

        public String getName() {
                return givenName;
        }

        public int getHungerLevel() {
                return hungerLevel;
        }

        public void setHungerLevel(int hungerLevel) {
                this.hungerLevel = hungerLevel;
                //could be a state change. Maybe you don't
                //need to eat until hunger lever is > 5?
        }

        public String toString() {
                return "customer " + getName();
        }

        public void setGui(LCustomerGui g) {
                customerGui = g;
        }

        public LCustomerGui getGui() {
                return customerGui;
        }

		
}
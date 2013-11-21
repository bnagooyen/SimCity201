package simcity.TTRestaurant;
/**
package restaurant;

import agent.Agent;
import restaurant.interfaces.Market; 

import java.util.*;

import restaurant.CustomerAgent.AgentEvent;

/**
 * Restaurant Market Agent
 */
/**
public class MarketAgent extends Agent implements Market {
        

        Map<String, Integer> Stock = new HashMap<String, Integer>(4);
        Map<String, Integer> RestaurantSupply = new HashMap<String, Integer>(4); 
        CookAgent cook;
        CashierAgent cashier; 
        Timer timer = new Timer();
        public double bill = 0; 
        private boolean checkingStock = false;
        Random randomQuan = new Random(); 

        public void msgBuyFood(Map<String, Integer> Supply, CookAgent c) {
                RestaurantSupply.putAll(Supply); 
                checkingStock = true; 
                cook = c; 
                stateChanged(); 
        }
        
        public void msgPaidForStock(double b) {
                print("Received payment from cashier for " + b); 
        }

        
        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
/**
        protected boolean pickAndExecuteAnAction() {
                if (checkingStock == true) {
                        returnSupply(); 
                }
                
     return false; 
                
        }

        // Actions

        private void returnSupply() {
                int i;
                boolean notEnoughStock = false; 
                //adding supply for Steak
                i = 5 - RestaurantSupply.get("Steak"); 
                if (i > Stock.get("Steak")) {
                        RestaurantSupply.put("Steak", Stock.get("Steak"));
                        bill += 6.99 * Stock.get("Steak"); 
                        Stock.put("Steak", 0);
                        print("Couldn't fufill stock of Steak");
                        notEnoughStock = true; 
                        print("The bill right now is " + bill); 
                }
                else if (i > 0) {
                        RestaurantSupply.put("Steak", i);
                        bill += 6.99 * (Stock.get("Steak") - i); 
                        Stock.put("Steak", Stock.get("Steak") - i); 
                        print("The bill right now is " + bill); 
                }
                
                //adding supply for Chicken
                i = 5 - RestaurantSupply.get("Chicken"); 
                if (i > Stock.get("Chicken") && i <= 5) {
                        RestaurantSupply.put("Chicken", Stock.get("Chicken"));
                        bill += 4.99 * Stock.get("Chicken"); 
                        Stock.put("Chicken", 0);
                        print("Couldn't fufill stock of Chicken");
                        notEnoughStock = true; 
                }
                else if (i > 0 && i <= 5) {
                        RestaurantSupply.put("Chicken", i);
                        bill = bill + (4.99 * (Stock.get("Chicken") - i)); 
                        Stock.put("Chicken", Stock.get("Chicken") - i); 
                }
                
                //adding supply for salad
                i = 5 - RestaurantSupply.get("Salad"); 
                if (i > Stock.get("Salad") && i <= 5) {
                        RestaurantSupply.put("Salad", Stock.get("Salad"));
                        bill += 1.99 * (Stock.get("Salad")); 
                        Stock.put("Salad", 0);
                        print("Couldn't fufill stock of Salad");
                        notEnoughStock = true; 
                }
                else if (i > 0) {
                        RestaurantSupply.put("Salad", i);
                        bill += 1.99 * (Stock.get("Salad") - i); 
                        Stock.put("Salad", Stock.get("Salad") - i);
                }
                
                //adding supply for pizza
                i = 5 - RestaurantSupply.get("Pizza"); 
                if (i > Stock.get("Pizza") && i <= 5) {
                        RestaurantSupply.put("Pizza", Stock.get("Pizza"));
                        bill += 2.99 *(Stock.get("Pizza")); 
                        Stock.put("Pizza", 0);
                        print("Couldn't fufill stock of Pizza");
                        notEnoughStock = true; 
                }
                else if (i > 0) {
                        RestaurantSupply.put("Pizza", i);
                        bill += 2.99 *(Stock.get("Pizza") - i); 
                        Stock.put("Pizza", Stock.get("Pizza") - i); 
                }
                timer.schedule(new TimerTask() {
                        public void run() {
                                cook.msgOrderFulfilled(RestaurantSupply);
                                sendBill();
                        }
                },
                8000);
                
                if (notEnoughStock) {
                        cook.msgUnfulfilledStock(); 
                }
                
        }
        
        public void sendBill() {
                print("The bill for supplies is " + bill); 
                cashier.msgPayForSupply(this, bill);
        }
        

        public void addFood() {
                Stock.put("Steak", randomQuan.nextInt(20)); 
                Stock.put("Pizza", randomQuan.nextInt(20)); 
                Stock.put("Salad", randomQuan.nextInt(20)); 
                Stock.put("Chicken", randomQuan.nextInt(20));
        }
        
        public void setCashier(CashierAgent c) {
                cashier = c;
        }


}
*/

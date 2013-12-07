package simcity.BRestaurant;
 
 import simcity.interfaces.KCustomer;
 import simcity.interfaces.KWaiter;
 
 public class BRotatingOrders {
   BWaiterRole w;
   String choice;
   int table;
   
   public BRotatingOrders(BWaiterRole w, String choice, int table) {
     this.w = w;
     this.choice = choice;
     this.table = table;
   }
     
 }
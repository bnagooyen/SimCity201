package simcity.BRestaurant;
import simcity.BRestaurant.*;
import simcity.interfaces.*;
import simcity.BRestaurant.gui.*;



public class BFood {
        String typeOfFood; 
        int cookTime;
        int quantity;
        int threshold;
        int price;
        int capacity;
        int amount;
        
        public BFood(String type, int capacity, int cookTime, int quantity, int threshold, int price, int amount){
            this.typeOfFood = type;
            
            this.capacity = capacity;
            this.cookTime = cookTime;
            this.quantity = quantity;
            this.threshold = threshold;
            this.price = price;
            this.amount=amount;
        }
}
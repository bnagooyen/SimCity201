package simcity.KRestaurant;

import java.util.*;


public class KMenu {
	public List<Food> foods = new ArrayList<Food>();
	
	public KMenu() {
		foods.add(new Food(15.99, "Steak"));
		foods.add(new Food(10.99, "Chicken"));
		foods.add(new Food(5.99, "Salad"));
		foods.add(new Food(8.99, "Pizza"));
	}
	
	public class Food{
		double price;
		String type;
		
		public Food(double p, String s) {
			price = p;
			type = s;
		}
	}
}

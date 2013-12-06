package simcity.Drew_restaurant.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_CookRole.Food;

public class Menu {
	
	public Menu(){
		choices.add("chicken");
		choices.add("steak");
		choices.add("pizza");
		choices.add("salad");
		
		//Initialize prices Map
		prices.put("chicken", 10.99);
		prices.put("steak", 15.99);
		prices.put("salad", 5.99);
		prices.put("pizza", 8.99);
	}
	
	public List<String> choices
	= new ArrayList<String>();
	
	private  Map<String,Double> prices = new HashMap<String, Double>();
	
	public String getItem(int ch){
		return choices.get(ch);
	}
	
	public void remove(String ch){
		choices.remove(ch);
	}
	
	public Double getPrice(String ch){
		return prices.get(ch.toLowerCase());
	}
}
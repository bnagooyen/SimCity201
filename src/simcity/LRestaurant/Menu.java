package simcity.LRestaurant;


import java.util.*;

//import restaurant.CookAgent.Order;

/**
 * Restaurant Menu
 */

public class Menu {
	
	public List<Food>menu = new ArrayList<Food>();
	
	public Menu(){
		menu.add(new Food(15, "St"));
		menu.add(new Food(10, "Ch"));
		menu.add(new Food(5, "S"));
		menu.add(new Food(8, "P"));
	}
	
	public void removeItem(String choice){
		if(choice.equals("St")){
			menu.remove(0);
		}
		else if(choice.equals("Ch")){
			menu.remove(1);
		}
		else if(choice.equals("S")){
			menu.remove(2);
		}
		else if(choice.equals("P")){
			menu.remove(3);
		}
	}
	
	public Boolean affordable(int money){
		for(Food f: menu){
			if(f.cost <=money){
				return true;
			}
		}
		
		return false;
	}
	
	public String pickOrder(int money){
		Random rand = new Random(); //randomly selecting what cust orders
		int i = rand.nextInt(4);
		int counter = 0;
		
		while(counter < 4){
			if(menu.get(i).cost <= money){
				return menu.get(i).item;
			}	 
			i = (i+1)%4;
		}
		
		return null;
	}
	
	public String poorPickOrder(){
		Random rand = new Random(); //randomly selecting what cust orders
		int i = rand.nextInt(4);
		
		return menu.get(i).item;
	}
	
	
	private class Food{
		int cost;
		String item;
		
		Food(int c, String i){
			cost = c;
			item = i;
		}
	}
}


package simcity.Market;

public class MFoodOrder {
	public String type;
	public int amount;
	public double price;
	
	public MFoodOrder(String t, int a) {
		type = t;
		amount = a;
		if(t.equals("Steak")) {
			price = 10;
		}
		else if(t.equals("Chicken")) {
			price = 7;
		}
		else if(t.equals("Salad")) {
			price = 3;
		}
		else if(t.equals("Pizza")) {
			price = 5;
		}
		
	}
}

package DRestaurant;

public class DMenu {
	
	public String[] myOptions = new String[]{"Steak", "Chicken", "Salad", "Pizza"};
	public int getSize() {
		return myOptions.length;
	}
	
	public double getPrice(String choice) {
		if(choice.equals("Steak")) {
			return 15.99;
		}
		if(choice.equals("Chicken")) {
			return 10.99;
		}
		if(choice.equals("Pizza")) {
			return 8.99;
		}
		if(choice.equals("Salad")) {
			return 5.99;
		}
		return 0.0;
	}
	
	public String MostExpensiveICanAfford(double money) {
		if(money>=15.99)
			return "Steak";
		else if(money>=10.99)
			return "Chicken";
		else if(money>=8.99)
			return "Pizza";
		else if(money>=5.99)
			return "Salad";
		return "None";
	}
	
	public String OutOf(String ch, double money) {
		if(money>=15.99 && !ch.equals("Steak"))
			return "Steak";
		else if(money>=10.99 && !ch.equals("Chicken"))
			return "Chicken";
		else if(money>=8.99 && !ch.equals("Pizza"))
			return "Pizza";
		else if(money>=5.99 && !ch.equals("Salad"))
			return "Salad";
		return "None";
	}
	
}

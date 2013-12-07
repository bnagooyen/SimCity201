package simcity.DRestaurant;

public class DFoodOrder  {
	private String food;
	private int val;
	public DFoodOrder(String f, int v) {
		food=f;
		val=v;
	}
	public String getFood() {
		return food;
	}
	public int getVal() {
		return val;
	}
}
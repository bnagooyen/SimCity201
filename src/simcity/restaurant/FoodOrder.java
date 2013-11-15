package simcity.restaurant;

public class FoodOrder  {
	private String food;
	private int val;
	public FoodOrder(String f, int v) {
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
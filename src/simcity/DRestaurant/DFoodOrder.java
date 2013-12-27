/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

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
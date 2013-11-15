package simcity.restaurant;

public class Food {

	private String choice;
	private int cookTime;
	private int amount;
	
	Food(String ch, int cT, int amt) {
		choice = ch;
		cookTime = cT;
		amount = amt;
	}
	
	public String getChoice() {
		return choice;
	}
	
	public int getCookTime() {
		return cookTime;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int a) {
		amount=a;
	}
	
}

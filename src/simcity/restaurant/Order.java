package simcity.restaurant;

public class Order {

		int tablenum;
		private String choice;
		private WaiterRole w;
		public enum OrderState {needsReOrder, pending, cooking, cooked, serving, billPending, billProcessed, grillInUse, ordered, clearPlating, plated, plating};
		OrderState state;

		Order(String c, int t, WaiterRole wa) 
		{
			choice=c;
			tablenum=t;
			w=wa;
			state=OrderState.pending;
		}
		public String getChoice() {
			return choice;
		}
		public void setChoice(String choice) {
			this.choice = choice;
		}
		public WaiterRole getWaiter() {
			return w;
		}
		public void setWaiter(WaiterRole w) {
			this.w = w;
		}
		
		public int getTablenum() {
			return tablenum;
		}

}
 
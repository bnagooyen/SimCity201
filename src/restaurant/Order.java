package restaurant;

public class Order {

		int tablenum;
		private String choice;
		private WaiterAgent w;
		public enum OrderState {needsReOrder, pending, cooking, cooked, serving, billPending, billProcessed, grillInUse, ordered, clearPlating, plated, plating};
		OrderState state;

		Order(String c, int t, WaiterAgent wa) //new Order(cust.getChoice(), cust.getTablenum(), this)
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
		public WaiterAgent getWaiter() {
			return w;
		}
		public void setWaiter(WaiterAgent w) {
			this.w = w;
		}
		
		public int getTablenum() {
			return tablenum;
		}

}
 
package simcity.DRestaurant;

public class DOrder {

		int tablenum;
		private String choice;
		private DWaiterRole w;
		public enum OrderState {needsReOrder, pending, cooking, cooked, serving, billPending, billProcessed, grillInUse, ordered, clearPlating, plated, plating};
		OrderState state;

		DOrder(String c, int t, DWaiterRole wa) 
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
		public DWaiterRole getWaiter() {
			return w;
		}
		public void setWaiter(DWaiterRole w) {
			this.w = w;
		}
		
		public int getTablenum() {
			return tablenum;
		}

}
 
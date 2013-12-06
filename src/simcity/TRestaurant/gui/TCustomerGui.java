package simcity.TRestaurant.gui;

import simcity.TTRestaurant.TCustomerRole;

import java.awt.*;

public class TCustomerGui implements TGui{

	private TCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	TWaiterGui waiter1; 

	//private HostAgent host;
	TRestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private int xWait, yWait; 
	private enum Command {noCommand, GoToSeat, waitInLine, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int yTable = 250;

	public TCustomerGui(TCustomerRole c, TRestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -20;
		yPos = -20;
		xDestination = xWait;
		yDestination = yWait;
		//maitreD = m;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void setWaitPosition(int l) {
		xWait = (l+2)*30; 
		yWait = 20; 
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		if (seatnumber == 1){
			xDestination = 50; 
		}
		else if (seatnumber == 2){
			xDestination = 200;
		}
		else if (seatnumber == 3){
			xDestination = 350; 
		}
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoWaitInLine() {
		xDestination = xWait; 
		yDestination = yWait; 
		command = Command.waitInLine; 
	}
}

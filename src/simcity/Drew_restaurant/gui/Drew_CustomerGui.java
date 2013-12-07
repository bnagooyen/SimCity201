package simcity.Drew_restaurant.gui;

import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.Drew_restaurant.Drew_HostRole.Table;
import simcity.gui.Gui;

import java.awt.*;

public class Drew_CustomerGui implements Gui{

	private Drew_CustomerRole agent = null;
	private boolean isPresent = false;
	public boolean isHungry = false;

	private Drew_HostRole host;
	//public RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, getInLine, GoToSeat, LeaveRestaurant, goToCashier};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int xCashier = 50;
	public static final int yCashier = 275;
	public static final int xLine = 350;
	public static final int yLine = 120;
	
	public static final int customerSize = 20;
	public static final int offScreen = -40;

	public Drew_CustomerGui(Drew_CustomerRole c/*, RestaurantGui gui*/, Drew_HostRole m) {
		agent = c;
		xPos = 400;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		host = m;
		//this.gui = gui;
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
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			else if (command==command.goToCashier) agent.atCashier();
			else if (command==command.getInLine) agent.inLine();
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, customerSize, customerSize);
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

	public void DoGoToSeat() {//later you will map seatnumber to table coordinates.
	}

	public void goTo(Dimension Dim){
		xDestination = Dim.width;
		yDestination = Dim.height;
		command = Command.GoToSeat;
	}
	
    public void goToCashier() {
    	xDestination = xCashier;
        yDestination = yCashier;
        command = Command.goToCashier;
    }
    
    public void DoGetInLine(int num) {
    	xDestination = xLine;
        yDestination = yLine;
        command = Command.getInLine;
    }
	
	public void DoExitRestaurant() {
		xDestination = offScreen;
		yDestination = offScreen;
		command = Command.LeaveRestaurant;
	}
}

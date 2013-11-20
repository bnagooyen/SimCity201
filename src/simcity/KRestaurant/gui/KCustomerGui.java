package simcity.KRestaurant.gui;

import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;

import java.awt.*;

public class KCustomerGui implements KGui{

	private KCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	KRestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, atRestaurant};
	private Command command=Command.noCommand;

    private final int tablew = 50;
    private final int tableh = 50;
    
    private final int waitingAreax = 10;
    private final int waitingAreay = 40;

	public static final int xTable1 = 200;
    public static final int yTable1 = 200;
    
    public static final int xTable2 = 350;
    public static final int yTable2 = 200;
    
    public static final int xTable3 = 500;
    public static final int yTable3 = 200;
	
    public static final int xTable4 = 650;
    public static final int yTable4 = 200;
    
	public final int width = 20;
	public final int height = 20;

	public int table;
	
	public KCustomerGui(KCustomerRole c, KRestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
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
//			else if(command == Command.atRestaurant) {
//				agent.msgAtRestaurant();
//			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(Color.BLACK);
        g.drawString("C", xPos + 5, yPos + 15);
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

	//public void GoTo(Dimension dim) {
	public void GoTo(int table) {
		
	}
	
	public void DoGoToRestaurant() {
		xDestination = waitingAreax;
		yDestination = waitingAreay - 30;
	}
	public void DoGoToWaiting(int pos) {
		xDestination = waitingAreax;
		yDestination = waitingAreay+22*pos;

		command = Command.atRestaurant;
	}
	
	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		table = seatnumber;
		if(seatnumber == 1)
		{
			xDestination = xTable1;
			yDestination = yTable1;
			command = Command.GoToSeat;
		}
		else if(seatnumber == 2)
		{
			xDestination = xTable2;
			yDestination = yTable2;
			command = Command.GoToSeat;
		}
		else if(seatnumber == 3)
		{
			xDestination = xTable3;
			yDestination = yTable3;
			command = Command.GoToSeat;
		}
		else if(seatnumber == 4)
		{
			xDestination = xTable4;
			yDestination = yTable4;
			command = Command.GoToSeat;
		}
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}

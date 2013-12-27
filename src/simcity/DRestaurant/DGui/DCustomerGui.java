/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.DRestaurant.DGui;

import simcity.gui.Gui;
import simcity.gui.SimCityGui;

import java.awt.*;
import java.util.Random;

import simcity.DRestaurant.DCustomerRole;


public class DCustomerGui implements Gui{

	private DCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean isServed = false;
	private boolean orderDisplayed=false;
	private String dispChoice;
	//private HostAgent host;
	SimCityGui gui;

    public static final int x_Offset = 25;
    
	private int xPos, yPos;
	private int xCard, yCard;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoToCashier, LeaveRestaurant, GoToHangout, GoToFront};
	private Command command=Command.noCommand;

    public static final int hangout_x=10;
    public static final int hangout_y=150;
	
	public static final int cashier_x = 0;
    public static final int cashier_y=-20;
	
	public static final int TABLE_gap=30;
	
    
    public static final int frontline_x=0;
    public static final int frontline_y=TABLE_gap;
   
    
    public static final int TABLESZ_xy = 50;
   
    public static final int nTABLES = 12;
    public static final int TABLES_perRow = 4;

	public DCustomerGui(DCustomerRole c, SimCityGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
	}

	@Override
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
			else if(command == Command.GoToCashier) {
				agent.msgAnimationArrivedAtCashier();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			else if(command==Command.GoToFront) {
				agent.msgAnimationArrivedAtFront();
			}
			command=Command.noCommand;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.BLACK);
		if(orderDisplayed) {
			if(isServed)
				g.drawString(dispChoice.substring(0, 2), xPos+25, yPos+40);
			else
				g.drawString(dispChoice.substring(0, 2)+"?", xPos+25, yPos+40);	
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
//	public void setHungry() {
//		isHungry = true;
//		agent.gotHungry();
//		setPresent(true);
//	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToHangout() {
		Random rand= new Random();
		//command=Command.GoToHangout; (not mandatory, can be interrupted)
		xDestination=hangout_x+rand.nextInt(45);
		yDestination=hangout_y+rand.nextInt(175);
	}
	
	public void DoGoToFront() {
		command=Command.GoToFront;
		
		xDestination=frontline_x;
		yDestination=frontline_y;
		
	}
	public void DoGoToSeat(int table, int seatnumber) {//later you will map seatnumber to table coordinates.
		//xDestination = xTable;
		//yDestination = yTable;
		
		xDestination = ((table -1)%TABLES_perRow*TABLESZ_xy*2) + TABLE_gap+ x_Offset;
        yDestination = ((table-1)/TABLES_perRow)*TABLESZ_xy*2+TABLE_gap;
		
		command = Command.GoToSeat;
	}
	
	public void DoDisplayOrderCard(String choice) {
		dispChoice=choice;
		orderDisplayed=true;
	}
	
	public void DoUpdateOrderCard() {
		isServed=true;
	}
	
	public void DoHideOrderCard() {
		orderDisplayed=false;
		isServed=false;
	}

	public void DoGoToCashier() {
		xDestination = cashier_x;
		yDestination = cashier_y;
//		System.out.println("dogotocashier called");
		command = Command.GoToCashier;
	}
	
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}

package simcity.BRestaurant.gui;




import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;


import java.awt.*;

public class BHostGui implements BGui {


	private BWaiterRole agent = null;
	private boolean requestBreak=false;
	private boolean isPresent=false;
	BRestaurantGui gui;
	private int mycounter;
	private int xPos = 10, yPos = 10;//default waiter position
	private int xDestination = 10, yDestination = 10;//default start position

	public static final int xTable = 50;
	public static final int yTable = 150;

	public static final int xTable1 = 200;
	public static final int yTable1 = 150;

	public static final int xTable2 = 350;
	public static final int yTable2 = 150;

	public BHostGui(BWaiterRole agent) {
		this.agent = agent;
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

		if (xPos == xDestination && yPos == yDestination
				& (xDestination == xTable + 20) & (yDestination == yTable - 20) || xPos == xDestination && yPos == yDestination
				& (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20) || xPos == xDestination && yPos == yDestination
				& (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20) ) {
			agent.msgAtTable();
		}
		
		
	}


	public BHostGui(BWaiterRole w, BRestaurantGui gui, int counter){ //HostAgent m) {
		agent = w;
		mycounter=counter;
		xPos = 10*counter;
		yPos = 10;
		xDestination = 10*counter;
		yDestination = 10;
		//maitreD = m;
		this.gui = gui;
	}


	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return true;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	
	public boolean isBreak() {
		return requestBreak;
	}

	public void DoBringToTable(BCustomer customer, int tableNumber) {

		if (tableNumber==1){
			xDestination = xTable + 20;
			yDestination = yTable - 20;

		}

		else if (tableNumber==2){
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;


		}

		else if (tableNumber==3){
			xDestination = xTable2 + 20;
			yDestination = yTable2 -20;

		}
		
		while(xPos != xDestination || yPos!= yDestination){
			System.out.println("");
			}

	}

	public void DoTakeOrder(BCustomer customer, int tableNumber) {

		if (tableNumber==1){
			xDestination = xTable + 20;
			yDestination = yTable - 20;

		}

		else if (tableNumber==2){
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;


		}

		else if (tableNumber==3){
			xDestination = xTable2 + 20;
			yDestination = yTable2 -20;

		}


	}


	public void DoGoToCustomer (int tableNumber) {

		if (tableNumber==1){
			xDestination = xTable + 20;
			yDestination = yTable - 20;

		}

		else if (tableNumber==2){
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;


		}

		else if (tableNumber==3){
			xDestination = xTable2 + 20;
			yDestination = yTable2 -20;

		}

		while(xPos != xDestination || yPos!= yDestination){
			System.out.println("");
			}
	}


	public void GoToCook(){
		xDestination = -50;
		yDestination = -50;

	}

	public void DoLeaveCustomer() {
		xDestination = 10*mycounter;
		yDestination = 10;
		while(xPos != xDestination || yPos!= yDestination){
			System.out.println("");
			}
		
	}
	
	public void DoGoToWaiting() {
		xDestination = 220;
		yDestination = 10;
		while(xPos != xDestination || yPos!= yDestination){
			System.out.println("");
			}
		
	}
	
	public void DoGoToPlating() {
		xDestination = 50;
		yDestination = 300;
		while(xPos != xDestination || yPos!= yDestination){
			System.out.println("");
			}
		
	}


	public void FinishedTakingOrder(){
		xDestination = -20;
		yDestination = -20;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
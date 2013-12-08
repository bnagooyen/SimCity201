package simcity.KRestaurant.gui;


import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.gui.Gui;
import simcity.interfaces.KWaiter;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class KWaiterGui implements Gui {

    private KWaiterRole agent = null;

    private int xPos = 200, yPos = -25;//default waiter position
    private int xDestination = -25, yDestination = -25;//default start position
    
    int homeposx = 200; int homeposy = 0;
    
    public static final int xTable1 = 100;
    public static final int yTable1 = 200;
    
    public static final int xTable2 = 225;
    public static final int yTable2 = 200;
    
    public static final int xTable3 = 350;
    public static final int yTable3 = 200;
    
    public static final int xTable4 = 475;
    public static final int yTable4 = 200;
    
    public int check; // check if host is back yet or not, 0 if free to take customer, 1 if busy
    public int table;
    
    private boolean isPresent;
    
    public KWaiterGui(KWaiterRole agent, int pos) {
        this.agent = agent;
        homeposx += pos*22;
        xPos = -20;
        yPos = -20;
        xDestination = homeposx;
        yDestination = homeposy;
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
    	if((xPos == 30) & (yPos == 30))
    	{
    		agent.msgCanGetCustomer();
    	}
        if(table == 1)
        {
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20)) {
	           agent.msgAtTable();
	        }
        }
        if(table == 2)
        {
        	 if (xPos == xDestination && yPos == yDestination
 	        		& (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
 	           agent.msgAtTable();                  
 	        }
        }
        if(table == 3)
        {
        	 if (xPos == xDestination && yPos == yDestination
 	        		& (xDestination == xTable3 + 20) & (yDestination == yTable3 - 20)) {
 	           agent.msgAtTable();
 	        }
        }
        if(table == 4)
        {
        	if (xPos == xDestination && yPos == yDestination
 	        		& (xDestination == xTable4 + 20) & (yDestination == yTable4 - 20)) {
 	           agent.msgAtTable();
        	}
        }
        if(table == 100) {
        	if(xPos == xDestination && yPos == yDestination) {
        		agent.msgAtCook();
        	}
        }
        if (table == 99) {
        	if(xPos == xDestination && yPos == yDestination) {
        		agent.msgGetFood();
        	}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("W", xPos + 5, yPos + 15);
    }
    	
    public boolean isPresent() {
        return true;
    }

    public void homePosIs(int pos) {
    	homeposx += pos*22;
    	homeposy = 0;
    }
    public void DoGetCustomer() {
    	xDestination = 30;
    	yDestination = 30;
    }
    
    public void DoBringToTable(int tableNumber){  //, Table table) {
    	table = tableNumber;
    	if (tableNumber == 1)
    	{
	        xDestination = xTable1 + 20;
	        yDestination = yTable1 - 20;
    	}
    	else if (tableNumber == 2)
    	{
    		xDestination = xTable2 + 20;
	        yDestination = yTable2 - 20;
    	}
    	else if (tableNumber == 3)
    	{
    		xDestination = xTable3 + 20;
	        yDestination = yTable3 - 20;	
    	}
    	else if (tableNumber == 4) {
    		xDestination = xTable4 +20;
    		yDestination = yTable4 -20;
    	}
    }

    public void DoGetFood(int place) {
       	xDestination = 435;
    	table = 99;
    	switch(place) {
    		case 0: yDestination = 5;
    			break;
    		case 1: yDestination = 25;
    			break;
    		case 2: yDestination = 45;
    			break;
    		case 3: yDestination = 65;
    			break;
    	}
    }
    public void DoLeaveCustomer() {
        xDestination = homeposx;
        yDestination = homeposy;
    }
    
    public void DoGoToWork() {
		xPos = -20;
		yPos = -20;
		xDestination = homeposx;
		yDestination = homeposy;
	}
    
    public void DoGoToTable(int tableNumber) {
    	table = tableNumber;
    	System.out.println("going to table");
    	if (tableNumber == 1)
    	{
	        xDestination = xTable1 + 20;
	        yDestination = yTable1 - 20;
    	}
    	else if (tableNumber == 2)
    	{
    		xDestination = xTable2 + 20;
	        yDestination = yTable2 - 20;
    	}
    	else if (tableNumber == 3)
    	{
    		xDestination = xTable3 + 20;
	        yDestination = yTable3 - 20;	
    	}
    	else if (tableNumber == 4)
    	{
    		xDestination = xTable4 +20;
    		yDestination = yTable4 - 20;
    	}
    }
    
    public void DoGoToCook() {
    	xDestination = 630;
    	yDestination = 0;
    	table = 100; // cook
    }
    public void DoLeaveCook() {
    	xDestination = 301;
    	yDestination = -21;
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void DoGoHome() {
		xDestination = -20;
    	yDestination = 0;
	}

	public void setPresent(boolean b) {
		isPresent = b;
	}
}

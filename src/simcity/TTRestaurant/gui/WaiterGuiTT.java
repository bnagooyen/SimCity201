package simcity.TTRestaurant.gui;


import simcity.TTRestaurant.TWaiterRole;

import java.awt.*;

public class WaiterGuiTT implements GuiTT {

    private TWaiterRole agent = null;
    
    private int xPos = 5, yPos = 0;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int xHome, yHome; 
    private int xCust, yCust; 
    private int currentTable;
    private boolean onBreak = false; 

    public static final int yTable = 250;
    RestaurantGuiTT gui;
    
    public WaiterGuiTT(TWaiterRole agent, RestaurantGuiTT gui) {
        this.agent = agent;
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
        
        if (xPos == 100 && yPos == -20) {
        	
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == (50 + (150 * (currentTable - 1))) + 20) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
        }
        
        if (xPos == 350 && yPos == 50)  {
        	agent.msgAtKitchen(); 
        }
        
        if (xPos == 20 && yPos == 20) {
        	agent.msgWithCustomer(); 
        }
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(int tableNum) {
    		currentTable = tableNum;
    		xDestination = 50 + (150 * (tableNum - 1)) + 20;
    		yDestination = yTable - 20;
    	}

    public void DoLeaveCustomer() {
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoGoToCook() {
    	xDestination = 350; 
    	yDestination = 50; 
    }
    
    public void getCustomer() {
    	xDestination = 20; 
    	yDestination = 20; 
    }
    
    public void DoGoOnBreak() {
    	xDestination = 100; 
    	yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setHome(int l) {
    	xHome = 20; 
    	yHome = (l+2)*30; 
    	DoLeaveCustomer(); 
    }
    
    public void setBreak() {
		onBreak = true;
	}
    
    public void offBreak() {
    	onBreak = false;
        this.gui = gui;
    }
    
	public boolean onBreak() {
		return onBreak;
	}
}
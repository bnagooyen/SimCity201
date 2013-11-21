package simcity.Drew_restaurant.gui;

import simcity.Drew_restaurant.interfaces.Cook;
import simcity.Drew_restaurant.HostRole;
import simcity.Drew_restaurant.HostRole.Table;

import java.awt.*;

public class CookGui implements Gui{

	private Cook agent = null;
	
	public RestaurantGui gui;
	boolean ispresent=true;
	boolean idle=false;
	boolean justArrived=false;
	
	public int windowCount=0;
	public int onWindow=0;
	public int onGrill=0;

	private int xPos, yPos;
	private int xDestination, yDestination;

	public static final int xGrill = 315;
	public static final int yGrill = 280;
	public static final int xWindow = 175;
	public static final int yWindow = 250;
	
	public static final int cookSize = 20;

	public CookGui(Cook c, RestaurantGui gui) {
		agent = c;
		xPos = xGrill;
		yPos = yGrill-40;
		xDestination = xGrill;
		yDestination = yGrill-40;
		this.gui = gui;
	}

	public void updatePosition() {
    	if(!idle & (xPos!=xDestination || yPos!=yDestination))justArrived=true;
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

        if (xPos == xDestination && yPos == yDestination & justArrived) {
        	agent.msgAtDest();
        	justArrived=false;
       }
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, cookSize, cookSize);
		g.setColor(Color.WHITE);
		g.drawString(""+onWindow, xWindow+50,yWindow-10);
		g.drawString(""+onGrill, xGrill,yGrill+35);
	}
	
    public void goToGrill() {
    	xDestination = xGrill;
        yDestination = yGrill;
        justArrived=true;
    }
    
    public void goToWindow() {
    	xDestination = xWindow+15*(windowCount%4);
        yDestination = yWindow;
        justArrived=true;
    }
    
    public boolean isPresent(){
    	return ispresent;
    }
}

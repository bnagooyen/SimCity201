package simcity.TRestaurant.gui;



import java.awt.*;

import agent.Role;
import simcity.TRestaurant.TCookRole;
import simcity.gui.Gui;

public class TCookGui implements Gui {

    private TCookRole agent = null;
    
    private int xPos = 420, yPos = 5;//default cook position
    private int xDestination = 420, yDestination = 5;//default start position

    public static final int yTable = 250;

    public TCookGui(Role r) {
        this.agent = (TCookRole) r;
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
        
        if (xPos == 400 && yPos == 50)  {
        	agent.msgAtCounter(); 
        }
        
    }
    
    public void makeFood() {
    	xDestination = 420; 
    	yDestination = 150; 
    }
    
    public void giveFoodToWaiter() {
    	xDestination = 400; 
    	yDestination = 50; 
    }
    
    public void goHome() {
    	xDestination = 420; 
    	yDestination = 5; 
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
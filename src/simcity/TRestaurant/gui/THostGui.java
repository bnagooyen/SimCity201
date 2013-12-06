package simcity.TRestaurant.gui;

import simcity.TTRestaurant.THostRole;

import java.awt.*;

public class THostGui implements TGui {

    private THostRole agent = null;
    
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position
    public boolean ready = true; 
    private int currentTable; 

    public static final int yTable = 250;

    public THostGui(THostRole agent) {
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
        		& (xDestination == (50 + (150 * (currentTable - 1))) + 20) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
        }
        
        if (xPos == 0 && yPos == 0) {
        	ready = true; 
        }
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
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
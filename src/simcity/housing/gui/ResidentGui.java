package simcity.housing.gui;


import simcity.PersonAgent;
import simcity.gui.Gui;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ResidentGui implements Gui {

    private PersonAgent agent = null;

    private int xPos = 280, yPos = 150;//default waiter position
    private int xDestination = 280, yDestination = 150;//default start position
    
    int homeposx = 280; int homeposy = 0;
    
    public ResidentGui(PersonAgent agent) {
        this.agent = agent;
        xPos = homeposx;
        yPos = 0;
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
        
        if (xPos == 0 && yPos == 0) {
        	agent.msgAtFridge();
        }
        
        if (xPos == 0 && yPos == 0) {
        	agent.msgAtGrill();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.orange);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.white);
        g.drawString("P", xPos + 5, yPos + 15);
    }
    	
    public boolean isPresent() {
        return true;
    }
    
    public void goToHomePosition() {
    	xDestination = 200; 
    	yDestination = 200; 
    }
    
    public void LeaveHouse() {
    	xDestination = 200;
    	yDestination = -40;
    }
    
    public void goToBed() {
    	xDestination = 15; 
    	yDestination = 20; 
    }
    
    public void makeFood() {
    	xDestination = 175; 
    	yDestination = 100; 
    }
    
    public void goToFridge() {
    	xDestination = 0; 
    	yDestination = 0; 
    }
    
    public void goToTable() {
    	xDestination = 200; 
    	yDestination = 250; 
    }
  
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}

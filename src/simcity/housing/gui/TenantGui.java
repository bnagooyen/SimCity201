package simcity.housing.gui;


import simcity.PersonAgent;
import simcity.gui.Gui;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TenantGui implements Gui {

    private PersonAgent agent = null;
    private int roomNum; 

    private int xPos = 150, yPos = 150;//default waiter position
    private int xDestination, yDestination;//default start position
    
    private int xBed, yBed; 
    private int xKitchen, yKitchen;
    private int xFridge, yFridge; 
    private int xTable, yTable;
    
    int homeposx = 200; int homeposy = 0;
    
    public TenantGui(PersonAgent agent, String index) {
        this.agent = agent;
        if (index.equals("A")) {
            homeposx = 100; 
            homeposy = 100;
            
            xBed = 130; 
            yBed = 120;
            
            xKitchen = 30; 
            yKitchen = 180; 
            
            xFridge = 30; 
            yFridge = 270; 
            
            xTable = 30; 
            yTable = 60; 
        }
        else if (index == "B") {
            homeposx = 270; 
            homeposy = 100;
            
            xBed = 190; 
            yBed = 180;
            
            xKitchen = 240; 
            yKitchen = 250; 
            
            xFridge = 330; 
            yFridge = 250; 
            
            xTable = 310; 
            yTable = 100; 
            
        }
        else if (index == "C") {
            homeposx = 440; 
            homeposy = 100;
        }
        
        xPos = homeposx;
        yPos = 0;
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
        
        if (xPos == xFridge && yPos == yFridge) {
        	agent.msgAtFridge(); 
        }
        
        if (xPos == xKitchen && yPos == yKitchen) {
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
    	xDestination = homeposx; 
    	yDestination = homeposy; 
    }
    
    public void LeaveHouse() {
    	xDestination = homeposx;
    	yDestination = -40;
    }
    
    public void goToBed() {
    	xDestination = xBed; 
    	yDestination = yBed; 
    }
    
    public void makeFood() {
    	xDestination = xKitchen; 
    	yDestination = yKitchen;
    }
    
    public void goToFridge() {
    	xDestination = xFridge; 
    	yDestination = yFridge; 
    }
    
    public void goToTable() {
    	xDestination = xTable; 
    	yDestination = yTable; 
    }
  
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}

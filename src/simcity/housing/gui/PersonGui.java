package simcity.housing.gui;

import simcity.interfaces.Person;

import java.awt.*;

public class PersonGui implements Gui {

    private Person agent = null;
    
    private int xPos = 420, yPos = 5;
    private int xDestination = 420, yDestination = 5;//default start position

    public static final int yTable = 250;

    public PersonGui(Person agent) {
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
        
    }
    
    public void makeFood() {
    	xDestination = 420; 
    	yDestination = 150; 
    }
    
    public void goToBed() {
    	xDestination = 15; 
    	yDestination = 15; 
    }
    
    public void eatFood() {
    	xDestination = 200; 
    	yDestination = 250; 
    }
    

    public void draw(Graphics2D g) {
        g.setColor(Color.pink);
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
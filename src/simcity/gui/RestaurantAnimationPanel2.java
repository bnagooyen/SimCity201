package simcity.gui;

import javax.swing.*;

import simcity.Drew_restaurant.gui.Drew_CookGui;
import simcity.Drew_restaurant.gui.Drew_CustomerGui;
import simcity.Drew_restaurant.gui.Drew_WaiterGui;
import simcity.gui.SimCityGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanel2 extends BuildingAnimationPanel implements ActionListener {

    private final int WINDOWX = 575;
    private final int WINDOWY = 325;
    
    public static final int xTable = 50;
    public static final int yTable = 50;
    public static final int tableSize = 50;
    
    //Constants for the kitchen
    public static final int xKitchen = 175;
    public static final int yKitchen = 225;
    public static final int xCashier = 50;
    public static final int yCashier = 275;
    public static final int roomSize = 50;
    public static final int windowSize = 100;
    public static final int xHome = 300;
    public static final int yHome = 180;
    
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public RestaurantAnimationPanel2(SimCityGui restaurantGui, String nm) {
    	super(restaurantGui);
    	//this.gui=gui;
    	setSize(WINDOWX, WINDOWY);
        //setVisible(true);
        
        //bufferSize = this.getSize();
// 
//    	Timer timer = new Timer(20, this );
//    	timer.start();
//    	timer.addActionListener(this);
    	restaurantGui.city.timer.addActionListener(this);
    	//name = nm;
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(xTable, yTable, tableSize, tableSize);//200 and 250 need to be table params
        g2.fillRect(xTable*3, yTable, tableSize, tableSize);
        g2.fillRect(xTable*5, yTable, tableSize, tableSize);
        g2.fillRect(xTable, yTable*3, tableSize, tableSize);
        
        //Make the Kitchen
        g2.setColor(Color.GREEN);
        g2.fillRect(xKitchen, yKitchen, 4*roomSize+25, 2*roomSize);
        g2.setColor(Color.BLACK);
        g.drawString("Kitchen", xKitchen, yKitchen+(2*roomSize));
        
        //Make plate window
        g2.setColor(Color.red);
        g2.fillRect(xKitchen, yKitchen, windowSize, roomSize/2);
        
        //Make Grill
        g2.setColor(Color.black);
        g2.fillRect(xKitchen+4*roomSize-windowSize, yKitchen+3*roomSize/2, windowSize, roomSize/2);
        
        //Make the Kitchen
        g2.setColor(Color.BLUE);
        g2.fillRect(xCashier, yCashier, roomSize, roomSize);
        g2.setColor(Color.WHITE);
        g.drawString("Cashier", xCashier, yCashier+(roomSize/2));
        
        //Make the Waiter Home
        g2.setColor(Color.black);
        g2.fillRect(xHome, yHome-roomSize+45, roomSize*2, roomSize);
        g2.setColor(Color.black);
        g.drawString("Waiter Home",xHome, yHome-roomSize+45);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }

    }

    public void addGui(Drew_CustomerGui gui) {
        guis.add((Gui) gui);
    }

    public void addGui(Drew_WaiterGui gui) {
        guis.add((Gui) gui);
    }
    
    public void addGui(Drew_CookGui gui) {
        guis.add((Gui) gui);
    }

	@Override
	public void addGui(Gui g) {
		// TODO Auto-generated method stub
		guis.add(g);
	}
}


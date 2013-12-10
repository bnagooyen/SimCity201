package simcity.gui;


import simcity.PersonAgent;
import simcity.gui.SimCityGui;
import simcity.interfaces.*;
import simcity.BRestaurant.*;
import simcity.BRestaurant.gui.BCustomerGui;
import simcity.BRestaurant.gui.BGui;
import simcity.BRestaurant.gui.BHostGui;
import simcity.BRestaurant.gui.BRestaurantGui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanel5 extends BuildingAnimationPanel implements ActionListener {

    private final int WINDOWX = 575;
    private final int WINDOWY = 325;
    private final int x=50;
    private final int y=150;
    private final int height=50;
    private final int width=50;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private BRestaurantGui gui;

    private List<Gui> guis = new ArrayList<Gui>();

    public RestaurantAnimationPanel5(SimCityGui restaurantGui, String nm) {
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
			
		      for(Gui gui : guis) {
		            if (gui.isPresent()) {
		                gui.updatePosition();
		            }
		        }
                repaint();  //Will have paintComponent called
        }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, height, width);//200 and 250 need to be table params
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(200, 150, height, width);
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(350, 150, height, width);
        
        g2.setColor(Color.BLUE);
        g2.fillRect(50, 300, 100, 30);
        
        g2.setColor(Color.RED);
        g2.fillRect(300, 300, 30, 30);
        g2.setColor(Color.RED);
        g2.fillRect(265, 300, 30, 30);
        
        g2.setColor(Color.RED);
        g2.fillRect(335, 300, 30, 30);
        
        
        
      
        
        


        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent() ) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(BCustomerGui gui) {
        guis.add((Gui) gui);
    }

    public void addGui(BHostGui gui) {
        guis.add((Gui) gui);
    }


	@Override
	public void addGui(Gui g) {
		guis.add(g);
		
	}
}
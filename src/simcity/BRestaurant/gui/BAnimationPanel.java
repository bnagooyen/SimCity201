package simcity.BRestaurant.gui;


import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class BAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 575;
    private final int WINDOWY = 325;
    private final int x=50;
    private final int y=150;
    private final int height=50;
    private final int width=50;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private BRestaurantGui gui;

    private List<BGui> guis = new ArrayList<BGui>();

    public BAnimationPanel() {
    	this.gui=gui;
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(5, this );
    	timer.start();
    }

    
	public void actionPerformed(ActionEvent e) {
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
        
        
        
      
        
        


        for(BGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(BGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(BCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(BHostGui gui) {
        guis.add(gui);
    }
}
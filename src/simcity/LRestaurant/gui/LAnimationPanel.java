package simcity.LRestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class LAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 575;
    private final int WINDOWY = 325;
    private final int PANELX = 0;
    private final int PANELY = 0;
    
    private final int TABLEX1 = 300;
    private final int TABLEY1 = 250;
    
    private final int TABLEX2 = 100;
    private final int TABLEY2 = 200;
    
    private final int TABLEX3 = 350;
    private final int TABLEY3 = 100;
    
    private final int TABLEX4 = 170;
    private final int TABLEY4 = 60;
    
    private final int CookPlatingX = 100;
    private final int CookPlatingY = 310;
    private final int PlatingW = 70;
    private final int PlatingH = 20;
    
    private final int CookCookingX = 150;
    private final int CookCookingY = 310;
    private final int CookingW = 20;
    private final int CookingH = 70;
    
    private final int CookRX = 100;
    private final int CookRY = 350;
    private final int RefrigW = 10;
    private final int RefrigH = 10;
    
    private final int TABLEW = 50;
    private final int TABLEH = 50;
    
    private final int delay = 10;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<LGui> guis = new ArrayList<LGui>();

    public LAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(delay, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(Color.WHITE);
        g2.fillRect(PANELX, PANELY, this.getWidth(), this.getHeight() );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEX1, TABLEY1, TABLEW, TABLEH);//200 and 250 need to be table params
        g2.fillRect(TABLEX2, TABLEY2, TABLEW, TABLEH);
        g2.fillRect(TABLEX3, TABLEY3, TABLEW, TABLEH);
        g2.fillRect(TABLEX4, TABLEY4, TABLEW, TABLEH);
        
        g2.setColor(Color.RED);
        g2.fillRect(CookPlatingX, CookPlatingY, PlatingW, PlatingH);
        g2.fillRect(CookCookingX, CookCookingY, CookingW, CookingH);
        
        g2.setColor(Color.cyan);
        g2.fillRect(CookRX, CookRY, RefrigW, RefrigH);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CookCookingX+2, CookCookingY+22, CookingW-8, CookingH-25);


        for(LGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(LGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(LCustomerGui gui) {
        guis.add(gui);
    }
    
	public void addGui(LWaiterGui gui) {
	    guis.add(gui);
	}
    
    public void addGui(LHostGui gui) {
        guis.add(gui);
    }
    public void addGui(LCookGui gui) {
        guis.add(gui);
    }

}
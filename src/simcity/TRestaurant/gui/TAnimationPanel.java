package simcity.TRestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class TAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 575;
    private final int WINDOWY = 325;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<TGui> guis = new ArrayList<TGui>();

    public TAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        int timerLength = 20; 
    	Timer timer = new Timer(timerLength, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        int beginX = 0; 
        int beginY = 0; 
        g2.setColor(getBackground());
        g2.fillRect(beginX, beginY, WINDOWX, WINDOWY );

        //Here is the table
        int w = 50; 
        int h = 50; 
        g2.setColor(Color.ORANGE);
        g2.fillRect(50, 250, w, h);//200 and 250 need to be table params
        g2.setColor(Color.ORANGE);
        g2.fillRect(200, 250, w, h);
        g2.setColor(Color.ORANGE);
        g2.fillRect(350, 250, w, h);
        
        int kitchenW = 150; 
        int kitchenH = 200; 
        g2.setColor(Color.lightGray); 
        g2.fillRect(370, 0, kitchenW, kitchenH);
        
        int grillW = 40; 
        int grillH = 50; 
        g2.setColor(Color.RED);
        g2.fillRect(440, 140, grillW, grillH);
        
        int plateW = 40;
        int plateH = 50; 
        g2.setColor(Color.BLACK);
        g2.fillRect(370, 30, plateW, plateH);

        for(TGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(TGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(TCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(THostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(TWaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(TCookGui gui) {
        guis.add(gui);
    }
}
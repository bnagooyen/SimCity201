/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */
package simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class RestaurantAnimationPanel3 extends BuildingAnimationPanel implements ActionListener {
	String name;
	private final int WINDOWX = 575;
	private final int WINDOWY = 325;
	public static final int x_Offset = 25;
	public static final int BTMX = 0, BTMY=0;
	public static final int nTABLES=4;
	//public static final int TABLE1x = 50, TABLE1y = 50; // 200, 250 orig
	public static final int TABLESZ_xy=50;
	public static final int TABLE_gap=30;
	public static final int TABLES_perRow = 4;

	public static final int allKitchenItems_x = 470;

	public static final int refrig_y = 260;
	public static final int refrig_xsz=40;
	public static final int refrig_ysz=60;

	public static final int grill_xsz= 40;
	public static final int grill_ysz = 30;

	public static final int grillPizza_y =220;
	public static final int grillChicken_y =180;
	public static final int grillSteak_y =140;
	public static final int grillSalad_y =100;
	public static final int plating_ysz=60;
	public static final int plating_x=30;


	private Image bufferImage;
	private Dimension bufferSize;

	private SimCityGui gui;

	private List<Gui> guis = new ArrayList<Gui>();

	public RestaurantAnimationPanel3(SimCityGui gui, String nm) {
		super(gui);
		name = nm;
		this.gui=gui;
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		bufferSize = this.getSize();

		//	    	Timer timer = new Timer(20, this );
		//	    	timer.start();
		gui.city.timer.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {


		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		repaint();  //Will have paintComponent called
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(BTMX, BTMY, WINDOWX, WINDOWY );

		//Here is the table
		g2.setColor(Color.ORANGE);
		for(int i=0; i<nTABLES; i++)
		{
			int fillx = (i%TABLES_perRow)*TABLESZ_xy*2 + TABLE_gap + x_Offset;
			int filly = (i/TABLES_perRow)*TABLESZ_xy*2 + TABLE_gap;
			g2.fillRect(fillx, filly, TABLESZ_xy, TABLESZ_xy);//200 and 250 need to be table params

		}

		//draw kitchen components
		g2.setColor(Color.cyan);
		g2.fillRect(allKitchenItems_x, refrig_y, refrig_xsz, refrig_ysz);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(allKitchenItems_x, grillPizza_y, grill_xsz, grill_ysz);
		g2.fillRect(allKitchenItems_x, grillChicken_y, grill_xsz, grill_ysz);
		g2.fillRect(allKitchenItems_x, grillSteak_y, grill_xsz, grill_ysz);
		g2.fillRect(allKitchenItems_x, grillSalad_y, grill_xsz, grill_ysz);

		g2.setColor(Color.pink);
		g2.fillRect(allKitchenItems_x, plating_x, grill_xsz, plating_ysz);

		//	        for(Gui gui : guis) {
		//	            if (gui.isPresent()) {
		//	                gui.updatePosition();
		//	            }
		//	        }

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	//	    public void addGui(CustomerGui gui) {
	//	        guis.add(gui);
	//	    }
	//
	//	    public void addGui(WaiterGui gui) {
	//	        guis.add(gui);
	//	    }
	//	    
	//	    public void addGui(CookGui gui) {
	//	        guis.add(gui);
	//	    }

	@Override
	public void addGui(Gui g) {
		// TODO Auto-generated method stub
		guis.add(g);
	}

}

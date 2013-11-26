package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class TSimCityPanel extends JPanel implements ActionListener, MouseListener {

	protected CityGui city;
	protected ArrayList<CityComponent> statics, movings;
	protected Color background;
	protected Timer timer;
	
	public TSimCityPanel(CityGui city) {
		this.city = city;
		statics = new ArrayList<CityComponent>();
		movings = new ArrayList<CityComponent>();
		timer = new Timer(50, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		moveComponents();
		drawComponents(g);
	}
	
	public void drawComponents(Graphics g) {
		for (CityComponent c:statics) {
			c.paint(g);
		}
		
		for (CityComponent c:movings) {
			c.paint(g);
		}
	}
	
	public void moveComponents() {
		for (CityComponent c:movings) {
			c.updatePosition();
		}
	}
	
	public void addStatic(CityComponent c) {
		statics.add(c);
	}
	
	public void addMoving(CityComponent c) {
		movings.add(c);
	}
	
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

}

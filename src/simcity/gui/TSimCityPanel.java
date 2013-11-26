package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.Timer;

import simcity.gui.SimCityPanel.Location;
import simcity.gui.SimCityPanel.MyLandlord;
import simcity.interfaces.Person;

public abstract class TSimCityPanel extends JPanel implements MouseListener {

	public static final int NUMAPTS = 12;
	public static final int NUMHOUSES = 15;
	public static final int NUMBANKS = 2;
	public static final int NUMMARKETS = 4;
	public static final int NUMRESTAURANTS = 6;
	public int mktCounter=1;
	public int bankCounter = 1;
	public int houseNumCounter=1;
	public int ApartmentsPerLandlord;
	public int aptNumCounter=1;
	public char aptLetCounter='A';
	
	private List<Location> restaurants = new ArrayList<Location>();
	private List<Location> banks = new ArrayList<Location>();
	private List<Location> markets = new ArrayList<Location>();
	
	private ArrayList<Person> people = new ArrayList<Person>();
	private ArrayList<MyLandlord> landlords = new ArrayList<MyLandlord>();
	static Scanner in;
	
	protected CityGui city;
	protected ArrayList<CityComponent> statics, movings;
	protected Color background;
	//protected Timer timer;
	
	public TSimCityPanel(CityGui city) {
		this.city = city;
		statics = new ArrayList<CityComponent>();
		movings = new ArrayList<CityComponent>();
		//timer = new Timer(50, this);
		//timer.start();
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

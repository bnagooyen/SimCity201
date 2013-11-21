package simcity.BRestaurant.gui;



import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class BListPanel extends JPanel implements ActionListener, KeyListener {

	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private List<JButton> list = new ArrayList<JButton>();
	private JTextField addPersonB = new JTextField("hi");
	private JTextField addWaiter = new JTextField("hi");
	public int counter=1;
	
	private JCheckBox hunger=new JCheckBox();
	JButton pausebutton=new JButton("Pause");


	private BRestaurantPanel restPanel;
	private String type;

	/**
	 * Constructor for ListPanel.  Sets up all the gui
	 *
	 * @param rp   reference to the restaurant panel
	 * @param type indicates if this is for customers or waiters
	 */
	public BListPanel(BRestaurantPanel rp, String type) {
		restPanel = rp;
		this.type = type;
		
		if (type=="Customers"){
		setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
		add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

		addPersonB.setMaximumSize(getPreferredSize());
		


		pausebutton.addActionListener(this);
		add(pausebutton);

		hunger.setText("Hungry?");
		add(hunger);


		addPersonB.addActionListener(this);
		addPersonB.addKeyListener(this);
		
		add(addPersonB);
		
		
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);  
		add(pane);
		}
		
		else if (type=="Waiters"){
			setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
			add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

			
			addWaiter.setMaximumSize(getPreferredSize());

			addWaiter.addActionListener(this);
			addWaiter.addKeyListener(this);
			
			add(addWaiter);
			
			
			view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
			pane.setViewportView(view);  
			add(pane);
			}
		
		
	}

	
	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPersonB) {
			// Chapter 2.19 describes showInputDialog()
			String name = addPersonB.getText();
			addPerson(name);


		}
		
		else if (e.getSource() == addWaiter) {
			// Chapter 2.19 describes showInputDialog()
			String name = addWaiter.getText();
			counter+=3;
			addWaiter(name);
		}

		else {
			for (JButton temp:list){
				if (e.getSource() == temp)
					restPanel.showInfo(type, temp.getText());
			}
		}

	}
	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 *
	 * @param name name of new person
	 */
	public void addPerson(String name) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
			restPanel.addPerson(type, name, hunger.isSelected());//puts customer on list
			restPanel.showInfo(type, name);//puts hungry button on panel
			validate();
		}
	}
		public void addWaiter(String name) {
			if (name != null) {
				JButton button = new JButton(name);
				button.setBackground(Color.white);

				Dimension paneSize = pane.getSize();
				Dimension buttonSize = new Dimension(paneSize.width - 20,
						(int) (paneSize.height / 7));
				button.setPreferredSize(buttonSize);
				button.setMinimumSize(buttonSize);
				button.setMaximumSize(buttonSize);
				button.addActionListener(this);
				list.add(button);
				view.add(button);
				restPanel.addWaiter(type, name, counter);
				restPanel.showInfo(type, name);//puts hungry button on panel
				validate();
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource()==addPersonB){
			if(addPersonB.getText().length()!=0)
				hunger.setEnabled(true);
			else 
				hunger.setEnabled(false);
		}





	}

	

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource()==addPersonB){
			if(addPersonB.getText().length()!=0)
				hunger.setEnabled(true);
			else 
				hunger.setEnabled(false);
		}


	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
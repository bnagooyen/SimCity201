package simcity.KRestaurant.gui;

import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class KListPanel extends JPanel implements ActionListener {

    public JScrollPane pane1 =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    public JScrollPane pane2 =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view1 = new JPanel();
    private JPanel view2 = new JPanel();
    private List<JButton> customerList = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JTextField enterCustomerName = new JTextField(15);
    private JLabel customers = new JLabel("Customers");

    private List<JButton> waiterList = new ArrayList<JButton>();
    private JButton addWaiterB = new JButton("Add");
    private JTextField enterWaiterName = new JTextField(15);
    private JLabel waiters = new JLabel("Waiters");
    
    private JButton Pause = new JButton("Pause");
    private boolean paused = false;
    
    private KRestaurantPanel restPanel;
    private String type;
    
    private JCheckBox stateCB; // hungry checkbox
    private boolean hungry = false;
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public KListPanel(KRestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BorderLayout(5,0));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
                
        addPersonB.addActionListener(this);
        addWaiterB.addActionListener(this);
        Pause.addActionListener(this);
        
        stateCB = new JCheckBox();
        stateCB.setVisible(true);
        stateCB.addActionListener(this);
        stateCB.setText("Hungry?");
        stateCB.setSelected(false);

        
        JPanel jpAddPerson = new JPanel();
        jpAddPerson.setLayout(new BorderLayout(5,0));
        jpAddPerson.add(customers, BorderLayout.NORTH);
        jpAddPerson.add(enterCustomerName, BorderLayout.CENTER);
        jpAddPerson.add(addPersonB, BorderLayout.EAST);
        jpAddPerson.add(stateCB, BorderLayout.SOUTH);
                
        view1.setLayout(new BoxLayout((Container) view1, BoxLayout.Y_AXIS));
        pane1.setViewportView(view1);
        view2.setLayout(new BoxLayout((Container) view2, BoxLayout.Y_AXIS));
        pane2.setViewportView(view2);
        
        JPanel addCustomers = new JPanel();
        addCustomers.setLayout(new BorderLayout(5,0));
        addCustomers.add(jpAddPerson, BorderLayout.NORTH);
        addCustomers.add(pane1, BorderLayout.CENTER);
        addCustomers.add(Pause, BorderLayout.SOUTH);

        JPanel jpAddWaiter = new JPanel();
        jpAddWaiter.setLayout(new BorderLayout(5,0));
        jpAddWaiter.add(waiters, BorderLayout.NORTH);
        jpAddWaiter.add(enterWaiterName, BorderLayout.CENTER);
        jpAddWaiter.add(addWaiterB, BorderLayout.EAST);
        
        JPanel addWaiters = new JPanel();
        addWaiters.setLayout(new BorderLayout(5,0));
        addWaiters.add(jpAddWaiter, BorderLayout.NORTH);
        addWaiters.add(pane2, BorderLayout.CENTER);
        
        JPanel adding = new JPanel();
        adding.setLayout(new FlowLayout());
        
        add(addCustomers, BorderLayout.WEST);
        add(addWaiters, BorderLayout.EAST);
        
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	String tempName = enterCustomerName.getText();
        	if( hungry == true){
            	addPerson(tempName, true);
        	}
        	else {
        		addPerson(tempName, false);
        	}
        	stateCB.setEnabled(true);
        }
        else if(e.getSource() == addWaiterB) {
        	String tempName = enterWaiterName.getText();
        	addWaiter(tempName);
        }
        else if (e.getSource() == stateCB) {
        		hungry = true;
            }
  
        else {
        	for (JButton temp:customerList){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        	for (JButton temp2 : waiterList) {
        		if (e.getSource() == temp2){
                    restPanel.showInfo("Waiter", temp2.getText());
                    restPanel.showInfo("Waiter", temp2.getText());
        		}
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
    public void addPerson(String name, boolean hungry) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane1.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            customerList.add(button);
            view1.add(button);
            restPanel.addPerson(type, name, hungry);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void addWaiter(String name) {
    	if (name != null) {
    		JButton button = new JButton(name);
    		button.setBackground(Color.white);
    		
    		Dimension paneSize = pane2.getSize();
    		Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            waiterList.add(button);
            view2.add(button);
            restPanel.addWaiter("Waiter", name);//puts waiter on list
            restPanel.showInfo("Waiter", name);
            validate();
    	}
    }
}

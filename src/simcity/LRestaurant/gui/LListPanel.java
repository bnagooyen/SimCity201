package simcity.LRestaurant.gui;

import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class LListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Customer");

    private LRestaurantPanel restPanel;
    private String type;
    
    private JTextField inputName = new JTextField(20);
    private JPanel addNamePanel = new JPanel();
    
    private JCheckBox hungry;

    //For Waiter
    public JScrollPane paneW =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewW = new JPanel();
    private List<JButton> listW = new ArrayList<JButton>();
    private JButton addWaiter = new JButton("Waiter");

   
    private JTextField inputNameW = new JTextField(20);
    private JPanel addNamePanelW = new JPanel();
    
    private int hGap = 5;
    private int vGap = 0;
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public LListPanel(LRestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        
        if(type.equals("Customers")){
	        //setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
	        setLayout(new BorderLayout(hGap,vGap));
	        addNamePanel.setLayout(new BorderLayout(hGap,vGap));
	        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

	        addPersonB.addActionListener(this);
	        addNamePanel.add(addPersonB, BorderLayout.WEST);
	        addNamePanel.add(inputName, BorderLayout.CENTER);
	        add(addNamePanel, BorderLayout.SOUTH);
	        //add(addPersonB,BorderLayout.WEST);

	        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
	        pane.setViewportView(view);
	        add(pane, BorderLayout.CENTER);

	        hungry = new JCheckBox("Hungry");
	        addNamePanel.add(hungry, BorderLayout.EAST);
        }
    
        //Create Waiter List
        if(type.equals("Waiters")){
	        setLayout(new BorderLayout(hGap,vGap));
	        addNamePanelW.setLayout(new BorderLayout(hGap,vGap));
	        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

	        addWaiter.addActionListener(this);
	        addNamePanelW.add(addWaiter, BorderLayout.WEST);
	        addNamePanelW.add(inputNameW, BorderLayout.CENTER);
	        add(addNamePanelW, BorderLayout.SOUTH);

	        viewW.setLayout(new BoxLayout((Container) viewW, BoxLayout.Y_AXIS));
	        paneW.setViewportView(viewW);
	        add(paneW, BorderLayout.CENTER);
        }
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	String temp = inputName.getText();
        	inputName.setText("");//clears text input
        	addPerson(temp);
        }
        else if (e.getSource() == addWaiter) {
        	String temp = inputNameW.getText();
        	inputNameW.setText("");
        	addWaiter(temp);
        }
        else {
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo("Customers", temp.getText());
            }
        	for (JButton temp:listW){
                if (e.getSource() == temp)
                    restPanel.showInfo("Waiters", temp.getText());
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
                    (int) (paneSize.height / 7)); //offset of button
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name, hungry.isSelected());//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void addWaiter(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = paneW.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7)); //offset of button
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            listW.add(button);
            viewW.add(button);
            restPanel.addWaiter("Waiters", name);//puts waiter on list
            restPanel.showInfo("Waiters", name);
            validate();
        }
    }
}
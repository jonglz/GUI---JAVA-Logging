package shippingstore;

import javax.swing.Box;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.util.logging.*;

/**
 * MainApp Class is used to create a GUI for user, 
 * enabling user to input data. 
 * @author Jonathan Gonzalez and Kyle Greer
 *
 */
public class MainApp {

	private ShippingStore ss;
	private JFrame frame;
	private JPanel bPanel = new JPanel();
	private JPanel textPanel = new JPanel();
	private static final Logger logger = Logger.getLogger(MainApp.class.getName());    
	private static final FileHandler filehandler = initFileHandler();
	public static JTextArea textArea = new JTextArea(100,50); 

	/**
	 * Creates "shippingstore.log" to logging users action
	 * @return filehandler actions.
	 */
	private static FileHandler initFileHandler() {
		FileHandler filehandler = null;
		try {
			filehandler = new FileHandler("ShippingStore.log");
			filehandler.setFormatter(new SimpleFormatter());
			logger.addHandler(filehandler);
			logger.setLevel(Level.FINEST);
			logger.log(Level.INFO, "Initiating Logging"); 
		} catch (IOException ex) {
			Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
		}
		return filehandler;
	}

	/**
	 * Constructor
	 */
	public MainApp() {
		ss = ShippingStore.readDatabase();
	}

	/**
	 * This method servers as the main interface between the program and the user.
	 * Creates Frame and Panel for GUI
	 * The method interacts with the user by printing out a set of options, and
	 * asking the user to select one.
	 */
	public void runSoftware() {
		//Frame Set Up
		frame = new JFrame("Shipping Store");
		frame.setVisible(true);
		frame.setSize(1020, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creation of Buttons
		JButton bShowPackage = new JButton("Show Packages");
		JButton bAddPackage = new JButton("Add Package");
		JButton bDeletePackage = new JButton("Delete Package");
		JButton bSearchPackage = new JButton("Search Package");
		JButton bDisplayUsers = new JButton("Display Users");
		JButton bAddUser = new JButton("Add User");
		JButton bUpdateUser = new JButton("Update User");
		JButton bCompleteDelivery = new JButton("Complete Delivery");
		JButton bShowTransactions = new JButton("Show Transactions");
		JButton bExitProgram = new JButton("Exit Program");
		
		// Action Listener for ShowPackage Button
		bShowPackage.addActionListener(new ActionListener() {      
			public void actionPerformed(ActionEvent e) {
				showAllPackages();
				logger.info("Displaying Packages");
			}
		});

		// Action Listener for bAddPackage Button
		bAddPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logger.info("Adding Package");
					addNewPackage();
					showAllPackages();
					logger.info("Package Added");
				} catch (NullPointerException ex) {
					logger.warning("Cancle Was Pressed: Package Not Created");
				} catch (BadInputException e1) {
					logger.warning("Duplicate Tracking Number: Package Not Created");
					textArea.append("\nTracking Number Already Exists\n");
				} 
			}
		});
		
		// Action Listener for bDeletePackage Button
		bDeletePackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logger.info("Deleting Package");
					deletePackage();
					showAllPackages();
					logger.info("Package Deleted");
				} catch (NullPointerException ex) {
					logger.warning("Cancle Was Pressed: Package Not Deleted");
				} catch (BadInputException e1) {
					logger.warning("Tracking Number Not Found: Package Not Deleted");
					textArea.append("\nTracking Number Not Found\n"); 
				}
			}
		});
		
		// Action Listener for bSearchPackage Button
		bSearchPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logger.info("Searching For Package");
					searchPackage();
					logger.info("Package Found");
				} catch (NullPointerException ex) {
					logger.warning("Cancle Was Pressed: Searching Canceled");
				} catch (BadInputException e1) {
					logger.warning("Tracking Number Not Found");
					textArea.append("\nTracking Number Not Found\n"); 
				}
			}
		});

		// Action Listener for bDisplayUsers Button
		bDisplayUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Displaying All Users");
				showAllUsers();
			}
		});

		// Action Listener for bAddUser Button
		bAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logger.info("Adding A New User");
					addNewUser();
					showAllUsers();
					logger.info("Added A New User");
				} catch (NullPointerException ex) {
					logger.warning("Cancel Was Pressed: User Not Added");
				} catch (BadInputException e1) {
					logger.warning("Unable To Add User: Input Not Valid");
					textArea.append("\nUnable To Add User: Input Not Valid\n"); 
				}
			}
		});

		// Action Listener for bUpdateUser Button
		bUpdateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logger.info("Updating User");
					updateUser();
					logger.info("Updated User");
				} catch (NullPointerException ex) {
					logger.warning("Cancel Was Pressed: User Not Added");
				}catch (BadInputException ex) {
					logger.warning("Unable To Update User");
				}
			}
		});

		// Action Listener for bCompleteDelivery Button
		bCompleteDelivery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logger.info("Completing Delivery");
					deliverPackage();
					showAllTransactions();
					logger.info("Delivery Completed");
				} catch (NullPointerException ex) {
					logger.warning("Cancel Was Pressed: User Not Added");
				}catch (BadInputException ex) {
					logger.warning("Unable To Complete Delivery");
				}
			}
		});

		// Action Listener for bShowTransactions Button
		bShowTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Displaying Transactions");
				showAllTransactions();
			}
		});

		// Action Listener for bExitProgram Button
		bExitProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ss.writeDatabase();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				logger.info("Program Exited");
			}
		});


		//  Add Buttons to Panel
		bPanel.add(bShowPackage,BorderLayout.SOUTH); 
		bPanel.add(bAddPackage);
		bPanel.add(bDeletePackage);
		bPanel.add(bSearchPackage);
		bPanel.add(bDisplayUsers);
		bPanel.add(bAddUser);
		bPanel.add(bUpdateUser);
		bPanel.add(bCompleteDelivery);
		bPanel.add(bShowTransactions);
		bPanel.add(bExitProgram);

		// Grid Crateationg for Buttons.
		bPanel.setLayout(new GridLayout(10,1));
		//Creates Text field
		textPanel.add(textArea);
		frame.add(bPanel, BorderLayout.WEST);
		frame.add(textPanel);
	}



	/**
	 * This method allows the user to enter a new package to the list
	 * database.
	 * @throws shippingstore.BadInputException bad input
	 */
	@SuppressWarnings("unused")
	public void addNewPackage() throws BadInputException {

		try {
			Integer[] PackgeOptions = {1, 2, 3, 4};
			int packageType = (Integer)JOptionPane.showInputDialog(null, ("Select Package Type:\n"
					+ "1. Envelope\n"
					+ "2. Box\n"
					+ "3. Crate\n"
					+ "4. Drum"), "Package Type", JOptionPane.QUESTION_MESSAGE, null, PackgeOptions, null);

			String ptn = JOptionPane.showInputDialog("\nEnter Tracking Number (string): ");
			if (!(ptn.length() == 5)) {
				throw new BadInputException("Tracking Number Should Be  5 Characters Long.");
			}
			if (ss.packageExists(ptn)) {
				throw new BadInputException("\nTrackin Number Already Exists");
			}
			if (ptn == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}


			String[] SpecOptions = {"Fragile", "Books", "Catalogs", "Do-not-bend", "N/A"};
			String specification = (String) JOptionPane.showInputDialog(null,("\nEnter Specification"), 
					"Specification", JOptionPane.QUESTION_MESSAGE, null, SpecOptions, null);
			if (specification == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}

			String[] MailOptions = {"First-Class", "Priority", "Retail", "Ground", "Metro"};
			String mailingClass = (String) JOptionPane.showInputDialog(null, ("\nEnter Mailing Class"), 
					"Mailing Class", JOptionPane.QUESTION_MESSAGE, null, MailOptions, null);
			if (mailingClass == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}


			if (packageType == 1) {
				int height = Integer.parseInt(JOptionPane.showInputDialog("\nEnter height (inch), (int): "));
				if (height < 0) {
					throw new BadInputException("Height of Envelope cannot be negative.");
				}

				int width = Integer.parseInt(JOptionPane.showInputDialog("\nEnter width (inch), (int): "));
				if (width < 0) {
					throw new BadInputException("Width of Envelope cannot be negative.");
				}

				ss.addEnvelope(ptn, specification, mailingClass, height, width);

			} else if (packageType == 2) {
				int dimension = Integer.parseInt(JOptionPane.showInputDialog("\nEnter largest dimension (inch), (int): "));
				if (dimension < 0) {
					throw new BadInputException("Largest dimension of Box cannot be negative.");
				}

				int volume = Integer.parseInt(JOptionPane.showInputDialog("\nEnter volume (inch^3), (int): "));
				if (volume < 0) {
					throw new BadInputException("Volume of Box cannot be negative.");
				}

				ss.addBox(ptn, specification, mailingClass, dimension, volume);

			} else if (packageType == 3) {

				float weight = Float.parseFloat(JOptionPane.showInputDialog("\nEnter maximum load weight (lb), (float): "));
				if (weight < 0.0f) {
					throw new BadInputException("Maximum load weight of Crate cannot be negative.");
				}


				String content = JOptionPane.showInputDialog("\nEnter content (string): ");

				ss.addCrate(ptn, specification, mailingClass, weight, content);

			} else if (packageType == 4) {

				String[] materialOptions = {"Plastic", "Fiber"};
				String material = (String) JOptionPane.showInputDialog(null,("\nEnter Material: "),"Material", 
						JOptionPane.QUESTION_MESSAGE, null, materialOptions, null);
				if (material == null) {
					throw new NullPointerException("\nCancel Was Pressed");
				}

				float diameter = Float.parseFloat(JOptionPane.showInputDialog("\nEnter diameter (float): "));

				if (diameter < 0.0f) {
					throw new BadInputException("Diameter of Drum cannot be negative.");
				}

				ss.addDrum(ptn, specification, mailingClass, material, diameter);

			} else {
				textArea.append("Unknown package type entered. Please try again.");
			}
		} catch (NumberFormatException e) {
			logger.info("Cancel is pressed");
		}

	}

	/**
	 * This method prints out all the package currently in the inventory, in a
	 * formatted manner.
	 */
	public void showAllPackages() {
		textArea.setText(ss.getAllPackagesFormatted());

	}

	/**
	 * This method allows the user to delete a package from the inventory
	 * database.
	 * @throws BadInputException 
	 */
	@SuppressWarnings("unused")
	public void deletePackage() throws BadInputException {
		String ptn = JOptionPane.showInputDialog("\nEnter Tracking Number Of Pacakge To Delete: ");
		if (ss.deletePackage(ptn)) 
			textArea.append("Package Deleted.");
		if (!(ptn.length() == 5) || !(ss.packageExists(ptn)))
			throw new BadInputException("\nTrackin Number Not Found");
		else if (ptn == null) 
			throw new NullPointerException("\nCancel Was Pressed");
	}

	/**
	 * This method allows the users to search for a package given its tracking number
	 * and then it prints details about the package.
	 * @throws BadInputException 
	 */
	@SuppressWarnings("unused")
	public void searchPackage() throws BadInputException {
		String ptn = JOptionPane.showInputDialog("\nEnter tracking number of package to search for (string): ");
		if (ss.packageExists(ptn))
			textArea.setText(ss.getPackageFormatted(ptn));
		if (!(ptn.length() == 5) || !(ss.packageExists(ptn)))
			throw new BadInputException("\nTrackin Number Not Found");
		else if (ptn == null) 
			throw new NullPointerException("\nCancel Was Pressed");
	}

	/**
	 * Prints out a list of all users in the database.
	 */
	public void showAllUsers() {
		textArea.setText(ss.getAllUsersFormatted());
	}

	/**
	 * Adding new User. 
	 * @throws BadInputException
	 */
	public void addNewUser() throws BadInputException{
		try {
			Integer [] userTypeOoptions = {1,2};
			int userType = (Integer) JOptionPane.showInputDialog(null,("Select user type:\n"
					+ "1. Customer\n"
					+ "2. Employee"),"User Type", JOptionPane.QUESTION_MESSAGE, null, userTypeOoptions, null);

			String firstName = JOptionPane.showInputDialog("\nEnter first name (string): ");
			if (firstName == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}

			String lastName = JOptionPane.showInputDialog("\nEnter last name (string): ");
			if (lastName == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}

			if (userType == 1) {
				String phoneNumber = JOptionPane.showInputDialog("\nEnter phone number (string): ");
				if (phoneNumber == null) {
					throw new NullPointerException("\nCancel Was Pressed");
				}

				String address = JOptionPane.showInputDialog("\nEnter address (string): ");
				if (address == null) {
					throw new NullPointerException("\nCancel Was Pressed");
				}
				ss.addCustomer(firstName, lastName, phoneNumber, address);

			} else if (userType == 2) {

				float monthlySalary = 0.0f;
				try {
					monthlySalary = Float.parseFloat(JOptionPane.showInputDialog(null, "\nEnter monthly salary (float): "));
					while (monthlySalary < 0.0f ) {
						monthlySalary = Float.parseFloat(JOptionPane.showInputDialog(null, "Monthly salary cannot be less than zero" +
								"\nEnter monthly salary (float): "));
					} 
				} catch (NumberFormatException e){
					throw new BadInputException("\nInvalid Input");
				}

				int ssn = 0;
				try {
					ssn = Integer.parseInt(JOptionPane.showInputDialog("\nEnter SSN (9-digital int): "));
					while (ssn < 10000000 || ssn > 999999999) {
						ssn = Integer.parseInt(JOptionPane.showInputDialog("\nEnter SSN (9-digital int): "));
					} 
				} catch (NumberFormatException e) {
					throw new BadInputException("\nInvalid Input");
				}

				int bankAccNumber = 0;
				try {
					bankAccNumber = Integer.parseInt(JOptionPane.showInputDialog("\nEnter bank account number (int): "));
					while (bankAccNumber < 0) {
						bankAccNumber = Integer.parseInt(JOptionPane.showInputDialog("\nEnter bank account number (int): "));
					}

				} catch (NumberFormatException e) {
					throw new BadInputException("\nInvalid Input");
				}
				ss.addEmployee(firstName, lastName, ssn, monthlySalary, bankAccNumber);
			} 
		} catch (NumberFormatException e) {
			textArea.append("Unknown user type. Please try again.");
		}
	} 


	/**
	 * This method can be used to update a user's information, given their user
	 * ID.
	 *
	 * @throws shippingstore.BadInputException
	 */
	public void updateUser() throws BadInputException {
		try {
			int userID = Integer.parseInt(JOptionPane.showInputDialog("\nEnter user ID: "));
			if (!ss.userExists(userID)) {
				textArea.append("User not found.");
				logger.warning("User Not Found");
				throw new BadInputException("\nUser Not Found");
			}

			String firstName = JOptionPane.showInputDialog("\nEnter first name (string): ");
			if (firstName == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}

			String lastName = JOptionPane.showInputDialog("\nEnter last name (string): ");
			if (lastName == null) {
				throw new NullPointerException("\nCancel Was Pressed");
			}

			if (ss.isCustomer(userID)) {
				String phoneNumber = JOptionPane.showInputDialog("\nEnter phone number (string): ");
				if (phoneNumber == null) {
					throw new NullPointerException("\nCancel Was Pressed");
				}

				String address = JOptionPane.showInputDialog("\nEnter address (string): ");
				if (address == null) {
					throw new NullPointerException("\nCancel Was Pressed");
				}
				ss.updateCustomer(userID, firstName, lastName, phoneNumber, address);

			} else { //User is an employee

				float monthlySalary = 0.0f;
				try {
					monthlySalary = Float.parseFloat(JOptionPane.showInputDialog(null, "\nEnter monthly salary (float): "));
					while (monthlySalary < 0.0f ) {
						monthlySalary = Float.parseFloat(JOptionPane.showInputDialog(null, "Monthly salary cannot be less than zero" +
								"\nEnter monthly salary (float): "));
					} 
				} catch (NumberFormatException e){
					throw new BadInputException("\nInvalid Input");
				}

				int ssn = 0;
				try {
					ssn = Integer.parseInt(JOptionPane.showInputDialog("\nEnter SSN (9-digital int): "));
					while (ssn < 10000000 || ssn > 999999999) {
						ssn = Integer.parseInt(JOptionPane.showInputDialog("\nEnter SSN (9-digital int): "));
					} 
				} catch (NumberFormatException e) {
					throw new BadInputException("\nInvalid Input");
				}

				int bankAccNumber = 0;
				try {
					bankAccNumber = Integer.parseInt(JOptionPane.showInputDialog("\nEnter bank account number (int): "));
					while (bankAccNumber < 0) {
						bankAccNumber = Integer.parseInt(JOptionPane.showInputDialog("\nEnter bank account number (int): "));
					}
				} catch (NumberFormatException e) {
					throw new BadInputException("\nInvalid Input");
				} 
				ss.updateEmployee(userID, firstName, lastName, ssn, monthlySalary, bankAccNumber);
			}
		} catch (NumberFormatException e) {
			throw new NullPointerException();
		}
	}

	/**
	 * This method is used to complete a package shipping/delivery transaction.
	 *
	 * @throws shippingstore.BadInputException
	 */
	public void deliverPackage() throws BadInputException {
		try {
			Date currentDate = new Date(System.currentTimeMillis());

			int customerId = Integer.parseInt(JOptionPane.showInputDialog("\nEnter customer ID (int): "));
			//Check that the customer exists in database
			boolean customerExists = ss.userExists(customerId);

			if (!customerExists) {
				textArea.setText("\nThe customer ID you have entered does not exist in the database.\n"
						+ "Please add the customer to the database first and then try again.");
				logger.warning("Invalid Input: Customer Does Not Exists");
				throw new BadInputException("\nInvalid Input: Customer Does Not Exists");
				

			}

			int employeeId = Integer.parseInt(JOptionPane.showInputDialog("\nEnter employee ID (int): "));

			//Check that the employee exists in database
			boolean employeeExists = ss.userExists(employeeId);

			if (!employeeExists) {
				textArea.setText("\nThe employee ID you have entered does not exist in the database.\n"
						+ "Please add the employee to the database first and then try again.");
				logger.warning("Invalid Input: Employee Does Not Exists");
				throw new BadInputException("\nInvalid Input: Employee Does Not Exists");
			}

			String ptn = JOptionPane.showInputDialog("\nEnter tracking number (string): ");

			//Check that the package exists in database
			if (!ss.packageExists(ptn)) {
				textArea.setText("\nThe package with the tracking number you are trying to deliver "
						+ "does not exist in the database. Aborting transaction.");
				logger.warning("Invalid Input: Package Does Not Exists");
				throw new BadInputException("\nInvalid Input: Package Does Not Exists");
			}

			float price = Float.parseFloat(JOptionPane.showInputDialog("\nEnter price (float): "));
			if (price < 0.0f) {
				throw new BadInputException("Price cannot be negative.");
			}

			ss.addShppingTransaction(customerId, employeeId, ptn, currentDate, currentDate, price);
			ss.deletePackage(ptn);

			textArea.append("\nTransaction Completed!");

		} catch (NumberFormatException e) {
			throw new NullPointerException();
		}
	}

	/**
	 * Method used to display Transactions
	 */
	public void showAllTransactions() {
		textArea.setText(ss.getAllTransactionsText());
	}

	/**
	 * Main Method - Creates MainApp Object
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		MainApp app = new MainApp();
		app.runSoftware();
	}
}
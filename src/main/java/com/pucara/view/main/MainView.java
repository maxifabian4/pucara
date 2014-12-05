package com.pucara.view.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;

import com.pucara.common.CommonData;
import com.pucara.core.database.MySqlAccess;

/**
 * Remove!
 * Principal view of the application.
 * 
 * @author Maximiliano
 */
public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	private HeaderView headerView;

	/**
	 * Removes the current panel in the frame.
	 */
	public void removeCentralPanel() {
		BorderLayout bl = (BorderLayout) this.getContentPane().getLayout();
		this.getContentPane()
				.remove(bl.getLayoutComponent(BorderLayout.CENTER));
	}

	/**
	 * Adds a new panel depending of the menu.
	 * 
	 * @param panel
	 */
	public void addNewCentralPanel(Component panel) {
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.validate();
	}

	/**
	 * Returns the main frame.
	 * 
	 * @return frame
	 */
	public JFrame getFrame() {
		return this;
	}

	/**
	 * Throws the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainView window = new MainView();
					window.setUndecorated(true);
					// window.setResizable(false);
					window.setExtendedState(JFrame.MAXIMIZED_BOTH);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Public constructor. Allows initialize all the components.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * 
	 */
	public void closeApplication() {
		// MySqlAccess.stopMySqlServer();
		System.exit(0);
	}

	/**
	 * Create a new listeners to the frame.
	 */
	private void generateListeners() {
		// Add a windows listener to manage the close windows event ...
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				closeApplication();
			}
		});
	}

	/**
	 * Initializes the frame's content.
	 */
	private void initialize() {
		this.getContentPane()
				.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Initialize a new connection to the database ...
		MySqlAccess.establishConection();

		// Set the form properties ...
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the Header panel ...
		headerView = new HeaderView(this);
		this.getContentPane().add(headerView, HeaderView.ORIENTATION);

		// Add the main panel by default ...
//		ReportView defaultPanel = new ReportView();
//		this.getContentPane().add(defaultPanel, BorderLayout.CENTER);

		// Create listeners for the frame ...
		generateListeners();
	}

}

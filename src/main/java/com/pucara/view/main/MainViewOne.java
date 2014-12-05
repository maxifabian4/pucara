package com.pucara.view.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.SaleSummaryPanel;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SwingListPanelOne;
import com.pucara.controller.main.MainControllerOne;
import com.pucara.view.render.CardPanelRendererOne;
import com.pucara.view.render.ProductSaleCellRenderer;

/**
 * Principal application view.
 * 
 * @author Maximiliano
 */
public class MainViewOne extends JFrame {
	private static final long serialVersionUID = 1L;
	// private JPanel centerPanel;
	private JPanel headerPanel;
	private JTextField inputTextField;
	private SwingListPanelOne cardList;
	private SwingListPanel partialProductsList;
	private SaleSummaryPanel summaryPanel;

	/**
	 * Public constructor. Allows initialize all the components.
	 */
	public MainViewOne() {
		// Decorated properties.
		this.setUndecorated(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());

		// Behavior properties.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add header to the content pane.
		headerPanel = createHeaderPanel();
		this.add(headerPanel, BorderLayout.PAGE_START);

		// Add center to the content pane.
		// JPanel centerPanel = createCenterPanel();
		this.add(createCenterPanel(), BorderLayout.CENTER);
	}

	public void displayComponents() {
		this.setVisible(true);
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
					MainViewOne window = new MainViewOne();
					MainControllerOne controller = new MainControllerOne(window);
					controller.displayView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void createNewTextField() {
		inputTextField = CommonUIComponents.createInputTextFieldOne();
		// inputTextField.addActionListener(actionListener);
		headerPanel.add(inputTextField);
	}

	public String getInputValue() {
		if (inputTextField == null) {
			return CommonData.EMPTY_STRING;
		} else {
			return inputTextField.getText();
		}
	}

	public void addNewCard(Component component) {
		cardList.addElement(component);
	}

	public void addPartialListToPanel(Object[] products) {
		if (partialProductsList == null) {
			partialProductsList = new SwingListPanel(products, null,
					new ProductSaleCellRenderer());
		} else if (products.length > 0) {
			partialProductsList.populateDataInTheList(products);
		}

		this.revalidate();
		this.repaint();
	}

	private JPanel createHeaderPanel() {
		ImagePanel headerPanel = new ImagePanel(new ImageIcon(
				"C:/Users/Maximiliano/Pictures/material.jpg").getImage());

		// Set properties to the panel.
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(new EmptyBorder(40, 80, 40, 80));

		return headerPanel;
	}

	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel();

		// Set properties to the panel.
		GridLayout layout = new GridLayout(1, 2);
		layout.setHgap(30);
		centerPanel.setLayout(layout);
		centerPanel.setBorder(new EmptyBorder(20, 80, 20, 80));
		centerPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		CommonUIComponents.applyScrollLookAndFeelProperties();
		partialProductsList = new SwingListPanel(new Object[] {}, null,
				new ProductSaleCellRenderer());
		leftPanel.add(partialProductsList, BorderLayout.CENTER);
		centerPanel.add(leftPanel);

		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		cardList = new SwingListPanelOne(new CardPanelRendererOne(null));

		JScrollPane pane = new JScrollPane(cardList);
		// Remove scroll border ...
		pane.setBorder(null);
		// Modify vertical scroll bar size ...
		JScrollBar sbV = pane.getVerticalScrollBar();
		sbV.setPreferredSize(new Dimension(7, 0));

		// Modify horizontal scroll bar size ...
		JScrollBar sbH = pane.getHorizontalScrollBar();
		sbH.setPreferredSize(new Dimension(7, 0));

		rightPanel.add(pane);
		centerPanel.add(rightPanel);

		return centerPanel;
	}

	class ImagePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image img;

		public ImagePanel(String img) {
			this(new ImageIcon(img).getImage());
		}

		public ImagePanel(Image img) {
			this.img = img;
			setLayout(new BorderLayout());
			setBackground(CommonData.DEFAULT_SELECTION_COLOR);
		}

		// public void paintComponent(Graphics g) {
		// // g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
		// }
	}

	public void cleanInputTextField() {
		inputTextField.setText(CommonData.EMPTY_STRING);
	}

	/**
	 * Retrieves components from view.
	 */

	public JTextField getInputTextField() {
		return inputTextField;
	}

	public boolean alreadyExistSummary() {
		return summaryPanel != null;
	}

	public void updateSummary(Object[] products) {
		if (summaryPanel != null) {
			summaryPanel.updateContent(products);
		}
	}

	public void createSummary(Object[] products) {
		summaryPanel = new SaleSummaryPanel(products);
		addNewCard(summaryPanel);
	}

}

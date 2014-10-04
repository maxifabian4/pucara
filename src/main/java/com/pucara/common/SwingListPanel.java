package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.pucara.core.entities.Product;
import com.pucara.core.generic.FillPainter;

public class SwingListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JList productList;
	private JLabel summaryLabel;

	public SwingListPanel(Object[] items, ProductView view,
			ListCellRenderer render) {
		/**
		 * Allows apply look and feel properties on scrolls.
		 */
		applyLookAndFeelProperties();

		setLayout(new BorderLayout());
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		productList = new JList(items);
		productList.setCellRenderer(render);
		productList.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		productList.getSelectionModel().addListSelectionListener(
				new SharedListSelectionHandler(view));

		JScrollPane pane = new JScrollPane(productList);
		// Remove scroll border ...
		pane.setBorder(null);
		// Modify vertical scroll bar size ...
		JScrollBar sbV = pane.getVerticalScrollBar();
		sbV.setPreferredSize(new Dimension(7, 0));

		// Modify horizontal scroll bar size ...
		JScrollBar sbH = pane.getHorizontalScrollBar();
		sbH.setPreferredSize(new Dimension(7, 0));

		add(pane, BorderLayout.CENTER);
		add(createSelectedItemsLabel(), BorderLayout.PAGE_END);
	}

	public void selectBarcodeOnList(String barcode) {
		int index = getIndex(barcode);
		productList.setSelectedIndex(index);
		productList.ensureIndexIsVisible(index);
	}

	public void selectItemByIndex(int index) {
		productList.setSelectedIndex(index);
		productList.ensureIndexIsVisible(index);
	}

	public void selectMultipleBarcodeOnList(List<Product> products) {
		int[] indices = new int[products.size()];
		int count = 0;

		for (Product product : products) {
			indices[count++] = getIndex(product.getBarcode());
		}

		productList.setSelectedIndices(indices);
		productList.ensureIndexIsVisible(indices[indices.length - 1]);

		// Enable label summary.
		summaryLabel.setVisible(true);
		summaryLabel.setText(String.format("productos seleccionados: %d",
				products.size()));
	}

	public void cleanSelection() {
		productList.clearSelection();
		summaryLabel.setVisible(false);
	}

	public void populateDataInTheList(Object[] listObjectProducts) {
		productList.setListData(listObjectProducts);
	}

	public void addListMouseListener(MouseListener listener) {
		productList.addMouseListener(listener);
	}

	public String getSelectedBarcode(int index) {
		ListModel model = productList.getModel();
		Product object = (Product) model.getElementAt(index);

		return object.getBarcode();
	}

	public String getSelectedSaleBarcode(int index) {
		ListModel model = productList.getModel();
		Product object = (Product) model.getElementAt(index);

		return object.getBarcode();
	}

	public int getNumberOfElements() {
		if (productList != null) {
			ListModel model = productList.getModel();
			return model.getSize();
		} else {
			return 0;
		}
	}

	public void addListKeyListener(KeyListener keyListener) {
		productList.addKeyListener(keyListener);
	}

	public void requestFocusOnList() {
		productList.requestFocus();
	}

	public void cleanSummaryLabel() {
		summaryLabel.setVisible(false);
	}

	private int getIndex(String barcode) {
		ListModel model = productList.getModel();
		Product object;
		int count = 0;

		object = (Product) model.getElementAt(count);

		while (count < model.getSize() && !object.getBarcode().equals(barcode)) {
			count++;

			if (count != model.getSize()) {
				object = (Product) model.getElementAt(count);
			}
		}

		if (count < model.getSize()) {
			return count;
		} else {
			return -1;
		}
	}

	/**
	 * Allows apply look and feel properties on scrolls.
	 */
	private void applyLookAndFeelProperties() {
		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:ScrollBarThumb[Enabled].backgroundPainter",
					new FillPainter(Color.LIGHT_GRAY));
			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter",
					new FillPainter(CommonData.MOUSEOVER_SCROLL_COLOR));
			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:ScrollBarTrack[Enabled].backgroundPainter",
					new FillPainter(CommonData.GENERAL_BACKGROUND_COLOR));

			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:\"ScrollBar.button\".size", 0);
			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar.decrementButtonGap", 0);
			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar.incrementButtonGap", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private Component createSelectedItemsLabel() {
		// Create panel.
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBorder(new EmptyBorder(20, 0, 0, 0));
		container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		summaryLabel = CommonUIComponents
				.createLabelForm(CommonData.EMPTY_STRING);
		summaryLabel.setVisible(false);

		container.add(summaryLabel);

		return container;
	}

}
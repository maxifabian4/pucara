package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import com.pucara.core.entities.Product;

public class SwingListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JList productList;

	public SwingListPanel(Object[] items, ProductView view,
			ListCellRenderer render) {
		/**
		 * Allows apply look and feel properties on scrolls.
		 */
		// applyLookAndFeelProperties();

		setLayout(new BorderLayout());
		this.setBackground(new Color(0, 0, 0, 1));
		// this.setPreferredSize(new Dimension(0, 100));
		productList = new JList(items);
		productList.setCellRenderer(render);
		productList.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		productList.getSelectionModel().addListSelectionListener(
				new SharedListSelectionHandler(view));

		JScrollPane pane = new JScrollPane(productList);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// Remove scroll border ...
		pane.setBorder(null);
		// Modify vertical scroll bar size ...
		JScrollBar sbV = pane.getVerticalScrollBar();
		sbV.setPreferredSize(new Dimension(7, 0));

		// Modify horizontal scroll bar size ...
		JScrollBar sbH = pane.getHorizontalScrollBar();
		sbH.setPreferredSize(new Dimension(7, 0));

		add(pane, BorderLayout.CENTER);
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

}
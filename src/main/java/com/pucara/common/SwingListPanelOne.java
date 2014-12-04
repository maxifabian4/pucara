package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

public class SwingListPanelOne extends JList {
	private static final long serialVersionUID = 1L;

	// private JList<Object> items;

	public SwingListPanelOne(ListCellRenderer render) {
		super(new DefaultListModel());
		// Panel properties.
		// this.setLayout(new BorderLayout());
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Allows apply look and feel properties on scrolls.
		// CommonUIComponents.applyScrollLookAndFeelProperties();

		// Create list and apply properties.
		// items = new JList(listModel);
		this.setCellRenderer(render);

//		JScrollPane pane = new JScrollPane(this);
//		// Remove scroll border ...
//		pane.setBorder(null);
//		// Modify vertical scroll bar size ...
//		JScrollBar sbV = pane.getVerticalScrollBar();
//		sbV.setPreferredSize(new Dimension(7, 0));
//
//		// Modify horizontal scroll bar size ...
//		JScrollBar sbH = pane.getHorizontalScrollBar();
//		sbH.setPreferredSize(new Dimension(7, 0));

//		this.add(pane, BorderLayout.CENTER);
	}

	public void populateDataInTheList(Object[] listObjects) {
		this.setListData(listObjects);
	}

	public void addElement(Object item) {
		DefaultListModel model = (DefaultListModel) this.getModel();
		model.addElement(item);
	}
}
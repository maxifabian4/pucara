package com.pucara.common;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class SharedListSelectionHandler implements ListSelectionListener {
	private ProductView view;

	// private int selectedIndex;

	public SharedListSelectionHandler(ProductView view) {
		this.view = view;
	}

	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();

		if (view != null) {
			view.saveSelectedIndex(lsm.getMinSelectionIndex());
		}
		// selectedIndex = lsm.getMinSelectionIndex();
		// System.out.println(">> " + selectedIndex);
	}

	// public int getSelectedIndex() {
	// return selectedIndex;
	// }

}
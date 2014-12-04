package com.pucara.view.render;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.pucara.view.render.object.CardPanelOne;

public class CardPanelRendererOne extends CardPanelOne implements
		ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public CardPanelRendererOne(JPanel component) {
		super(component);
	}

	@Override
	public JPanel getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		CardPanelOne card = new CardPanelOne((JPanel) value);

		if (isSelected) {
			card.selectionMode(true);
		} else {
			card.selectionMode(false);
		}

		return card;
	}
}

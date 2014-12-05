package com.pucara.view.render.object;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.pucara.common.CommonData;

public class CardPanelOne extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel component;

	public CardPanelOne(JPanel component) {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 0, 20, 0));
		this.setBackground(new Color(0, 0, 0, 1));

		if (component != null) {
			this.add(component, BorderLayout.CENTER);
			this.component = component;
		}
	}

	public void selectionMode(boolean isSelected) {
		if (isSelected) {
			MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 3, 0,
					0, CommonData.DEFAULT_SELECTION_COLOR);
			component.setBorder(matteBorder);
		} else {
			MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 3, 0,
					0, CommonData.GENERAL_BACKGROUND_COLOR);
			component.setBorder(matteBorder);
		}
	}
}

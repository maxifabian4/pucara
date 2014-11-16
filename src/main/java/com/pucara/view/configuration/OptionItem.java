package com.pucara.view.configuration;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;

public class OptionItem extends JPanel {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Component> components;

	public OptionItem(String description) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(new EmptyBorder(15, 20, 0, 20));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		components = new HashMap<String, Component>();

		// Add description to the panel.
		JLabel descriptionLabel = new JLabel(description);
		descriptionLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 14));
		descriptionLabel.setForeground(CommonData.DARK_FONT_COLOR);

		this.add(descriptionLabel);
		this.add(CommonUIComponents.createNewVerticalSeparatorBox(10));
	}

	public void addComponent(String key, Component value) {
		this.components.put(key, value);
		this.add(value);
	}

	public HashMap<String, Component> getAllComponents() {
		return components;
	}
}

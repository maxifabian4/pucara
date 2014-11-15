package com.pucara.view.configuration;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pucara.common.CommonData;

public class ConfigurationItem extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<OptionItem> optionItems;

	public ConfigurationItem(String title) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// this.setBorder(new EmptyBorder(30, 30, 30, 30));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		optionItems = new ArrayList<OptionItem>();

		// Add description to the panel.
		JLabel descriptionLabel = new JLabel(title);
		descriptionLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.BOLD, 14));
		descriptionLabel.setForeground(CommonData.DARK_FONT_COLOR);

		this.add(descriptionLabel);
	}

	public void addOptionItem(OptionItem item) {
		this.optionItems.add(item);
		this.add(item);
	}

}

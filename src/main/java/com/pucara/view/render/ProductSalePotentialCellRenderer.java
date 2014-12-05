package com.pucara.view.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.core.entities.Product;

/**
 * REMOVE!
 * @author Maximiliano Fabian
 */
public class ProductSalePotentialCellRenderer extends JPanel implements
		ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public ProductSalePotentialCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		removeAll();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 0));
		setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		Product entry = (Product) value;

		// Creates labels.
		JLabel description = new JLabel(entry.getDescription());
		description.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
				15));

		// Create icon container.
		JPanel iconContainer = new JPanel();
		iconContainer.setLayout(new GridBagLayout());
		iconContainer.setBackground(new Color(250, 250, 250, 1));

		ImageIcon icon = CommonUIComponents
				.createImageIcon(CommonData.IMAGES_PATH + "empty11x10.png");
		JLabel iconLabel = new JLabel("", icon, JLabel.LEFT);
		iconLabel.setBackground(new Color(0, 0, 255, 0));
		iconLabel.setOpaque(true);
		iconContainer.add(iconLabel);
		iconContainer.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(10));

		if (entry.getStock().equals(0)) {
			iconContainer.setBackground(new Color(0, 0, 255, 1));
			iconLabel.setIcon(CommonUIComponents
					.createImageIcon(CommonData.IMAGES_PATH
							+ "withoutstock11x10.png"));
			description.setForeground(Color.GRAY);
		}

		// Create center container.
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Add labels to the center container.
		container.add(description, Component.LEFT_ALIGNMENT);

		if (isSelected) {
			setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
			MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 2,
					0, CommonData.DEFAULT_SELECTION_COLOR);
			container.setBorder(matteBorder);
			description.setForeground(CommonData.DEFAULT_SELECTION_COLOR);
		} else {
			setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
			MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1,
					0, Color.LIGHT_GRAY);
			container.setBorder(matteBorder);
		}

		this.add(iconContainer, BorderLayout.LINE_START);
		this.add(container, BorderLayout.CENTER);

		return this;
	}

}

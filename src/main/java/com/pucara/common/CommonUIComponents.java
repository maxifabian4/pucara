package com.pucara.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.pucara.core.generic.FillPainter;

/**
 * Contains the common components used in the application.
 * 
 * @author Maximiliano
 */
public class CommonUIComponents {

	public static final int VERTICAL_STRUT_VALUE = 20;
	public static final String BARCODE = "c\u00F3digo";
	public static final String DESCRIPTION = "descripci\u00F3n";
	public static final String INITIAL_COST = "costo inicial";
	public static final String FINAL_COST = "costo final";
	public static final String PERCENTAGE = "porcentaje";
	public static final String STOCK = "stock";
	public static final String MIN_STOCK = "stock m\u00EDnimo";
	public static final String CATEGORY = "categor\u00EDa";
	public static final String BY_PERCENTAGE = "por porcentaje";
	public static final String PURCHASE_COST = "costo";
	public static final String PURCHASE_DESCRIPTION = "descripci\u00F3n";

	/**
	 * Creates a new button used by default in the system.
	 * 
	 * @param text
	 * @return JButton
	 */
	public static JButton createDefaultSystemButton(String text) {
		JButton defaultButton = new JButton(text);

		Border empty = new EmptyBorder(10, 15, 10, 15);
		defaultButton.setBorder(empty);
		defaultButton.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 17));
		defaultButton.setForeground(CommonData.LIGHT_FONT_COLOR);
		defaultButton.setBackground(CommonData.DEFAULT_BUTTON_COLOR);

		return defaultButton;
	}

	/**
	 * Creates a simple box, in order to split components vertically.
	 * 
	 * @param size
	 * @return Box
	 */
	public static Box createNewVerticalSeparatorBox(int size) {
		Box separatorBox = Box.createVerticalBox();
		separatorBox.add(Box.createVerticalStrut(size));

		return separatorBox;
	}

	/**
	 * Creates a simple box, in order to split components horizontally.
	 * 
	 * @param size
	 * @return Box
	 */
	public static Box createNewHorizontalSeparatorBox(int size) {
		Box separatorBox = Box.createHorizontalBox();
		separatorBox.add(Box.createHorizontalStrut(size));

		return separatorBox;
	}

	// remove it
	public static JTextField createNewTextField() {
		JTextField textFieldComponent = new JTextField();

		textFieldComponent.setBorder(new EmptyBorder(5, 5, 5, 5));
		textFieldComponent.setBackground(Color.red);
		textFieldComponent.setForeground(CommonData.DARK_FONT_COLOR);
		textFieldComponent.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, CommonData.GENERAL_FONT_SIZE));
		textFieldComponent.setColumns(10);

		return textFieldComponent;
	}

	/**
	 * Creates a new title for an specific view/panel.
	 * 
	 * @param text
	 * @return JLabel
	 */
	public static JLabel createNewViewTitle(String text) {
		JLabel lbl = new JLabel(text);

		lbl.setForeground(CommonData.DARK_FONT_COLOR);
		lbl.setFont(new Font(CommonData.GENERAL_FONT, Font.BOLD,
				CommonData.GENERAL_FONT_SIZE_TITLE_VIEW));

		return lbl;
	}

	/**
	 * Creates a new label as a menu bar.
	 * 
	 * @param createSaleListener
	 * @return Label
	 */
	public static JLabel createMenuLabel(String title, MouseListener listener) {
		JLabel label = new JLabel(title);

		label.setForeground(CommonData.DARK_FONT_COLOR);
		label.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
				CommonData.GENERAL_FONT_SIZE_LABEL));
		label.setAlignmentX(Label.CENTER);

		label.addMouseListener(listener);

		return label;
	}

	public static JLabel createMenuIconLabel(String file,
			MouseListener mouseListener) {
		ImageIcon icon = createImageIcon(CommonData.IMAGES_PATH + file);
		JLabel label = new JLabel(CommonData.EMPTY_STRING, icon, JLabel.CENTER);
		label.addMouseListener(mouseListener);
		label.setBackground(new Color(0, 255, 255, 0));
		label.setOpaque(true);

		return label;
	}

	/**
	 * 
	 * 
	 * @param comboName
	 * @return JComboBox
	 */
	public static JComboBox createNewComboBox(Object[] items) {
		JComboBox combo = new JComboBox(items);

		combo.setBorder(new EmptyBorder(7, 7, 7, 7));
		combo.setForeground(CommonData.DARK_FONT_COLOR);
		combo.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		combo.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
				CommonData.GENERAL_FONT_SIZE));

		return combo;
	}

	/**
	 * Create a new mouse listener in order to set a cursor in the textField.
	 * 
	 * @param textFieldSearch
	 * @param searchFieldMessage
	 * 
	 * @return MouseListener
	 */
	public static MouseListener createMouseTextfieldListener(
			final JTextField textFieldSearch, final String searchFieldMessage) {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldSearch.getText().equals(searchFieldMessage)) {
					textFieldSearch.setText(CommonData.EMPTY_STRING);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		};
	}

	public static MouseListener createMouseDisabledTextfieldListener(
			final JTextField textField) {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				textField.setEnabled(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		};
	}

	/**
	 * 
	 * @param actionListener
	 * @param keyListener
	 * @return
	 */
	public static JTextField createInputTextField(
			ActionListener actionListener, KeyListener keyListener) {
		JTextField textFieldComponent = new JTextField(CommonData.EMPTY_STRING);

		Border empty = new EmptyBorder(0, 0, 7, 0);
		MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.LIGHT_GRAY);

		textFieldComponent.setBorder(BorderFactory.createCompoundBorder(empty,
				matteBorder));
		textFieldComponent.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		textFieldComponent.setForeground(CommonData.DARK_FONT_COLOR);
		textFieldComponent.setSelectedTextColor(CommonData.LIGHT_FONT_COLOR);
		textFieldComponent.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, CommonData.GENERAL_FONT_SIZE_LABEL));

		if (actionListener != null) {
			textFieldComponent.addActionListener(actionListener);
		}

		if (keyListener != null) {
			textFieldComponent.addKeyListener(keyListener);
		}

		return textFieldComponent;
	}

	/**
	 * 
	 * @param text
	 * @param style
	 * @return
	 */
	public static JLabel createReportLabel(String text, int style, int size,
			Color fontColor) {
		JLabel label = new JLabel(text);

		label.setForeground(fontColor);
		label.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, style, size));

		return label;
	}

	/**
	 * Allows apply look and feel properties on scrolls.
	 */
	public static void applyScrollLookAndFeelProperties() {
		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:ScrollBarThumb[Enabled].backgroundPainter",
					new FillPainter(Color.WHITE));
			// new FillPainter(CommonData.GENERAL_BACKGROUND_COLOR));
			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter",
					// new FillPainter(CommonData.GENERAL_BACKGROUND_COLOR));
					new FillPainter(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put(
					"ScrollBar:ScrollBarTrack[Enabled].backgroundPainter",
					// new FillPainter(CommonData.GENERAL_BACKGROUND_COLOR));
					new FillPainter(Color.WHITE));
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

	public static JLabel createLabelForm(String string) {
		JLabel label = new JLabel(string);

		label.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 17));
		label.setForeground(CommonData.DARK_FONT_COLOR);

		return label;
	}

	public static Component createNewCheckBox(String label,
			boolean byPercentage, ActionListener actionListener) {
		JCheckBox checkBox = new JCheckBox(label);

		checkBox.setSelected(byPercentage);
		checkBox.setForeground(CommonData.DARK_FONT_COLOR);
		checkBox.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		checkBox.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 17));

		if (actionListener != null) {
			checkBox.addActionListener(actionListener);
		}

		return checkBox;
	}

	public static Component createNewCheckBoxForOptions(String label,
			boolean selected) {
		JCheckBox checkBox = new JCheckBox(label);

		checkBox.setSelected(selected);
		checkBox.setForeground(CommonData.DARK_FONT_COLOR);
		checkBox.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		checkBox.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 14));

		return checkBox;
	}

	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = CommonUIComponents.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static JTextField createInputTextFieldOne() {
		JTextField textFieldComponent = new JTextField();

		Border empty = new EmptyBorder(7, 9, 7, 9);
		textFieldComponent.setBorder(empty);
		textFieldComponent.setBackground(Color.WHITE);
		textFieldComponent.setForeground(Color.GRAY);
		textFieldComponent.setSelectedTextColor(CommonData.LIGHT_FONT_COLOR);
		textFieldComponent.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 17));

		return textFieldComponent;
	}

}

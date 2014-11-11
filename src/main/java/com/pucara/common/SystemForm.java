package com.pucara.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.pucara.persistence.domain.Category;

/**
 * This class creates a new panel that represents a new form.
 * 
 * @author Maximiliano Fabian
 */
public class SystemForm extends JPanel {
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Component> components;

	public SystemForm(String[] keys, String[] texts) {
		applyPanelProperties();
		components = new Hashtable<String, Component>();
		createComponentsForForm(keys, texts);
	}

	/**
	 * 
	 * @param pos
	 * @return Component
	 */
	public Component getComponentFormAt(String key) {
		return components.get(key);
	}

	public static JTextField applyUnselectedProperties(
			final JTextField inputBarcode) {
		Border empty = new EmptyBorder(0, 0, 7, 0);
		MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.LIGHT_GRAY);
		inputBarcode.setBorder(BorderFactory.createCompoundBorder(empty,
				matteBorder));

		return inputBarcode;
	}

	public static JTextField applySelectedProperties(
			final JTextField inputBarcode) {
		Border empty = new EmptyBorder(0, 0, 7, 0);
		MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 0, 2, 0,
				CommonData.DEFAULT_SELECTION_COLOR);
		inputBarcode.setBorder(BorderFactory.createCompoundBorder(empty,
				matteBorder));

		return inputBarcode;
	}

	public void addComboBox(String key, Category[] categories,
			Integer selectedCategory) {
		Component component = createComboElementForm(key, categories,
				selectedCategory);

		this.add(component, Component.LEFT_ALIGNMENT);
	}

	public void addConfirmButton(String key, ActionListener actionPerformed) {
		Component component = createButtonElementForm(key, actionPerformed);

		this.add(component, Component.LEFT_ALIGNMENT);
	}

	public void cleanTextFields(String[] keys) {
		for (int i = 0; i < keys.length; i++) {
			JTextField textField = (JTextField) components.get(keys[i]);
			textField.setText(CommonData.EMPTY_STRING);
		}
	}

	public void addKeyListenerAllFields(String[] keys, KeyListener keyListener) {
		for (int i = 0; i < keys.length; i++) {
			JTextField textField = (JTextField) components.get(keys[i]);
			textField.addKeyListener(keyListener);
		}
	}

	public List<String> getAllTextFieldValues(String[] keys) {
		List<String> values = new ArrayList<String>();

		for (int i = 0; i < keys.length; i++) {
			JTextField textField = (JTextField) components.get(keys[i]);
			values.add(textField.getText());
		}

		return values;
	}

	public void setActionListenerToComponent(String[] textFieldKeys,
			ActionListener listener) {
		setActionListenerToComponent(textFieldKeys, listener,
				CommonData.EMPTY_STRING);
	}

	public void setActionListenerToComponent(String[] textFieldKeys,
			ActionListener listener, String except) {
		JTextField textField;

		for (int i = 0; i < textFieldKeys.length; i++) {
			textField = (JTextField) components.get(textFieldKeys[i]);

			if (!textFieldKeys[i].equals(except)) {
				textField.addActionListener(listener);
			}
		}
	}

	public void addCheckBox(String label, boolean byPercentage,
			ActionListener actionListener) {
		Component component = CommonUIComponents.createNewCheckBox(label,
				byPercentage, actionListener);

		components.put(label, component);

		this.add(CommonUIComponents.createNewVerticalSeparatorBox(10));
		this.add(component, Component.LEFT_ALIGNMENT);
		this.add(CommonUIComponents.createNewVerticalSeparatorBox(10));
	}

	private void createComponentsForForm(String[] keys, String[] texts) {
		for (int i = 0; i < keys.length; i++) {
			this.add(createTextField(keys[i], texts[i]),
					Component.LEFT_ALIGNMENT);
		}
	}

	private void applyPanelProperties() {
		// Set style ...
		this.setBorder(new EmptyBorder(15, 15, 15, 15));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	private Component createComboElementForm(String text, Category[] items,
			Integer selectedCategory) {
		JPanel container = new JPanel();

		container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JLabel label = CommonUIComponents.createLabelForm(text);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setForeground(CommonData.DARK_FONT_COLOR);

		JComboBox combo = CommonUIComponents.createNewComboBox(items);
		combo.setAlignmentX(Component.LEFT_ALIGNMENT);

		if (selectedCategory != null) {
			for (Category category : items) {
				if (category.getId().equals(selectedCategory)) {
					combo.setSelectedItem(category);
				}
			}
		}

		container.add(label);
		container.add(CommonUIComponents.createNewVerticalSeparatorBox(10));
		container.add(combo);
		container.add(CommonUIComponents.createNewVerticalSeparatorBox(20));

		components.put(text, combo);

		return container;
	}

	private Component createTextField(String labelText, String textFieldText) {
		JPanel container = new JPanel();

		container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JLabel label = CommonUIComponents.createLabelForm(labelText);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		final JTextField textField = CommonUIComponents.createInputTextField(
				null, null);
		textField.setText(textFieldText);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				applySelectedProperties(textField);
			}

			@Override
			public void focusLost(FocusEvent e) {
				applyUnselectedProperties(textField);
			}
		});

		container.add(label);
		container.add(CommonUIComponents.createNewVerticalSeparatorBox(5));
		container.add(textField);
		container.add(CommonUIComponents.createNewVerticalSeparatorBox(20));

		components.put(labelText, textField);

		return container;
	}

	private Component createButtonElementForm(String text,
			ActionListener actionPerformed) {
		JPanel container = new JPanel();

		container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JButton button = CommonUIComponents.createDefaultSystemButton(text);
		button.setAlignmentX(LEFT_ALIGNMENT);

		if (actionPerformed != null) {
			button.addActionListener(actionPerformed);
		}

		container.add(CommonUIComponents.createNewVerticalSeparatorBox(10));
		container.add(button);
		container.add(CommonUIComponents.createNewVerticalSeparatorBox(10));

		components.put(text, button);

		return container;
	}

}

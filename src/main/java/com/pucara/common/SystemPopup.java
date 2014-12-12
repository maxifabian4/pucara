package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Maximiliano Fabian
 */
public class SystemPopup extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int CONFIRMATION = 0;

	// private SystemForm form;

	// /**
	// * Should be removed ...
	// *
	// * @param keys
	// * @param values
	// */
	// public SystemPopup(String[] keys, String[] values) {
	// /**
	// * Apply properties to the frame.
	// */
	// applyFrameProperties();
	//
	// /**
	// * Configure a flow layout to the frame.
	// */
	// configureFrameLayout();
	//
	// /**
	// * Add a simple form to the content panel.
	// */
	// addComponentsToFrame(keys, values);
	// }

	/**
	 * Use it as a generic mode.
	 */
	public SystemPopup(Component component, String title, int mode) {
		/**
		 * Apply properties to the frame.
		 */
		applyFrameProperties();

		/**
		 * Configure a flow layout to the frame.
		 */
		configureFrameLayout();

		this.getContentPane().add(createAllStuff(component, title, mode));
	}

	// public void addConfirmButton(String key, ActionListener actionListener) {
	// form.addConfirmButton(key, actionListener);
	// }

	// public void addKeyListenerAllFields(String[] keys,
	// KeyListener createKeyListener) {
	// form.addKeyListenerAllFields(keys, createKeyListener);
	// }
	//
	// public List<String> getAllTextFieldValues(String[] keys) {
	// return form.getAllTextFieldValues(keys);
	// }

	// public void setActionListenerToComponent(String[] textFieldKeys,
	// ActionListener listener) {
	// form.setActionListenerToComponent(textFieldKeys, listener);
	// }

	// public void addComboBox(Category[] categories, Integer selectedCategory)
	// {
	// form.addComboBox(CommonUIComponents.CATEGORY, categories,
	// selectedCategory);
	// }

	/**
	 * 
	 * @param values
	 * @param keys
	 * @param okAction
	 * @param components
	 * @param title
	 */
	// private void addComponentsToFrame(String[] keys, String[] values) {
	// form = new SystemForm(keys, values);
	// this.getContentPane().add(form);
	// }

	/**
	 * 
	 */
	private void configureFrameLayout() {
		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().setBackground(Color.WHITE);
	}

	/**
	 * 
	 */
	private void applyFrameProperties() {
		this.setFocusable(true);
		this.setUndecorated(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setBackground(new Color(.3f, .3f, .3f, .3f));
		this.setAlwaysOnTop(true);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				SystemPopup.this.dispose();
			}
		});
	}

	private Component createAllStuff(Component component, String title, int mode) {
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		JPanel header = new JPanel();
		header.setLayout(new BorderLayout());
		header.setBorder(new EmptyBorder(10, 10, 10, 10));
		header.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(CommonData.DARK_FONT_COLOR);
		titleLabel
				.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 17));

		header.add(titleLabel);

		container.add(header, BorderLayout.PAGE_START);
		container.add(component, BorderLayout.CENTER);

		/**
		 * Add buttons section.
		 */
		container.add(createButtonsPanel(mode), BorderLayout.PAGE_END);

		return container;
	}

	private JPanel createButtonsPanel(int mode) {
		JPanel buttonPanel = new JPanel();
		// Apply properties to the panel.
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		buttonPanel.setBackground(Color.WHITE);

		switch (mode) {
		case CONFIRMATION:
			JLabel okLabel = CommonUIComponents.createSystemLabelOne("ACEPTAR");
			JLabel cancelLabel = CommonUIComponents
					.createSystemLabelOne("CANCELAR");

			buttonPanel.add(okLabel);
			buttonPanel.add(CommonUIComponents
					.createNewHorizontalSeparatorBox(10));
			buttonPanel.add(cancelLabel);

			break;
		default:
			break;
		}

		return buttonPanel;
	}

}

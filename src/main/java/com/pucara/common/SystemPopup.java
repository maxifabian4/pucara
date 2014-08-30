package com.pucara.common;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JFrame;

/**
 * 
 * @author Maximiliano Fabian
 */
public class SystemPopup extends JFrame {

	private static final long serialVersionUID = 1L;
	private SystemForm form;

	public SystemPopup(String[] keys, String[] values) {
		/**
		 * Apply properties to the frame.
		 */
		applyFrameProperties();

		/**
		 * Configure a flow layout to the frame.
		 */
		configureFrameLayout();

		/**
		 * Add a simple form to the content panel.
		 */
		addComponentsToFrame(keys, values);
	}

	public void addConfirmButton(String key, ActionListener actionListener) {
		form.addConfirmButton(key, actionListener);
	}

	public void addKeyListenerAllFields(String[] keys, KeyListener createKeyListener) {
		form.addKeyListenerAllFields(keys, createKeyListener);
	}

	public List<String> getAllTextFieldValues(String[] keys) {
		return form.getAllTextFieldValues(keys);
	}

	public void setActionListenerToComponent(String[] textFieldKeys, ActionListener listener) {
		form.setActionListenerToComponent(textFieldKeys, listener);
	}

	/**
	 * 
	 * @param values
	 * @param keys
	 * @param okAction
	 * @param components
	 * @param title
	 */
	private void addComponentsToFrame(String[] keys, String[] values) {
		form = new SystemForm(keys, values);
		this.getContentPane().add(form);
	}

	/**
	 * 
	 */
	private void configureFrameLayout() {
		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().setBackground(CommonData.BACKGROUND_TABLE_COLOR);
	}

	/**
	 * 
	 */
	private void applyFrameProperties() {
		this.setFocusable(true);
		this.setUndecorated(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setBackground(new Color(.3f, .3f, .3f, .3f));
		this.setVisible(true);
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

}

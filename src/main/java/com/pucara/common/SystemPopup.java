package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	public static final int OK = 1;
	public static final int CANCEL = 0;
	private JLabel okLabel;

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

		this.okLabel = CommonUIComponents.createSystemLabelOne("ACEPTAR");
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
			JLabel cancelLabel = CommonUIComponents
					.createSystemLabelOne("CANCELAR");
			cancelLabel
					.addMouseListener(createNewMouseListenerForCancel(cancelLabel));

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

	private MouseListener createNewMouseListenerForCancel(
			final JLabel cancelLabel) {
		return new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				cancelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				SystemPopup.this.dispose();
			}
		};
	}

	public void addMouseListener(MouseListener listener) {
		okLabel.addMouseListener(listener);
	}

	public void setCursorForLabel(Cursor newCursor) {
		okLabel.setCursor(newCursor);
	}

}

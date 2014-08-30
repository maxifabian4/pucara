package com.pucara.core.generic;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

import com.pucara.common.CommonData;

/**
 * TODO Remove.
 * @author pucara
 *
 */
public class TextBoxFocusListener extends FocusAdapter {

	public TextBoxFocusListener() {
	}

	public void gainLost(FocusEvent evt) {
		JTextField component = (JTextField) evt.getSource();

		component.setBackground(CommonData.DEFAULT_SELECTION_COLOR);
		component.setForeground(CommonData.LIGHT_FONT_COLOR);
		component.setCaretColor(CommonData.LIGHT_FONT_COLOR);
	}

	public void focusLost(FocusEvent evt) {
		JTextField component = (JTextField) evt.getSource();

		component.setBackground(CommonData.LIGHT_FONT_COLOR);
		component.setForeground(CommonData.DARK_FONT_COLOR);
		component.setCaretColor(CommonData.DARK_FONT_COLOR);
	}

}

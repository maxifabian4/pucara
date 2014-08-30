package com.pucara.core.generic;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.Painter;

/**
 * This class allows customize a component.
 *  
 * @author Maximiliano
 */
public class FillPainter implements Painter<JComponent> {
	private final Color color;

	public FillPainter(Color c) {
		color = c;
	}

	@Override
	public void paint(Graphics2D g, JComponent object, int width, int height) {
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}

}

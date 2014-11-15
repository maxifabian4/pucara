package com.pucara.view.configuration;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;

public class ConfigurationView extends JPanel {
	private static final long serialVersionUID = 1L;

	public ConfigurationView() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(new EmptyBorder(30, 30, 30, 30));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JLabel title = CommonUIComponents
				.createNewViewTitle("Opciones generales");

		this.add(title);
		this.add(CommonUIComponents.createNewVerticalSeparatorBox(30));
	}

	public void generateAllOptions(ActionListener actionListener) {
		// Add options to the view.
		ConfigurationItem exportConfiguration = new ConfigurationItem(
				"Exportar");

		// Add option items for entities.
		OptionItem choseEntitiesOption = new OptionItem(
				"Elige que entidades deseas exportar (Por defecto se guardar\u00E1 en formato CSV):");
		choseEntitiesOption.addComponent("Productos", CommonUIComponents
				.createNewCheckBoxForOptions("Productos", true));
		choseEntitiesOption.addComponent("Categor\u00EDas", CommonUIComponents
				.createNewCheckBoxForOptions("Categor\u00EDas", true));
		choseEntitiesOption.addComponent("Proveedores", CommonUIComponents
				.createNewCheckBoxForOptions("Proveedores", true));

		exportConfiguration.addOptionItem(choseEntitiesOption);

		// Add chosen button.
		OptionItem chosenButtonOption = new OptionItem(
				"Seleccionar carpeta de destino:");
		JButton destinationButton = CommonUIComponents
				.createDefaultSystemButton("Seleccionar carpeta");
		destinationButton.addActionListener(actionListener);
		chosenButtonOption.addComponent("Seleccionar carpeta",
				destinationButton);

		exportConfiguration.addOptionItem(chosenButtonOption);

		this.add(exportConfiguration);
	}

}

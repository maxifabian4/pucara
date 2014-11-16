package com.pucara.view.configuration;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;

public class ConfigurationView extends JPanel {
	private static final long serialVersionUID = 1L;
	private OptionItem choseEntitiesOption;

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
		choseEntitiesOption = new OptionItem(
				"Elige que entidades deseas exportar (Por defecto se guardar\u00E1 en formato CSV):");
		choseEntitiesOption.addComponent("product", CommonUIComponents
				.createNewCheckBoxForOptions("Productos", true));
		choseEntitiesOption.addComponent("category", CommonUIComponents
				.createNewCheckBoxForOptions("Categor\u00EDas", true));
		choseEntitiesOption.addComponent("supplier", CommonUIComponents
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

	public List<String> getSelectedEntities() {
		HashMap<String, Component> components = choseEntitiesOption
				.getAllComponents();
		List<String> entities = new ArrayList<String>();

		for (String key : components.keySet()) {
			JCheckBox checkBox = (JCheckBox) components.get(key);

			if (checkBox.isSelected()) {
				entities.add(key);
			}
		}

		return entities;
	}
}

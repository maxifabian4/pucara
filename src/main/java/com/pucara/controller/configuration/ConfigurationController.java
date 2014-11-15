package com.pucara.controller.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import com.pucara.core.services.configuration.ConfigurationService;
import com.pucara.view.configuration.ConfigurationView;

public class ConfigurationController {
	private ConfigurationView view;

	public ConfigurationController(ConfigurationView view) {
		this.view = view;

		this.view.generateAllOptions(createExportEntitiesListener());
	}

	public ActionListener createExportEntitiesListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();

				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Seleccione una carpeta");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// disable the "All files" option.
				chooser.setAcceptAllFileFilterUsed(false);
				//
				if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
					ConfigurationService.exportEntity("products", chooser
							.getSelectedFile().getPath());
				}
			}

		};
	}
}

package com.pucara.controller.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.pucara.core.generic.Utilities;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.Response;
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

				if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
					List<String> entities = view.getSelectedEntities();

					if (entities.size() > 0) {
						Response response = exportDataFor(entities, chooser
								.getSelectedFile().getPath());

						if (response.wasSuccessful()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Las entidades seleccionadas se han exportado correctamente.",
											"Datos exportados",
											JOptionPane.INFORMATION_MESSAGE);
						} else {
							List<ErrorMessage> errors = response
									.getErrorsMessages();
							String generalErrorMessage = "Error al exportar datos:\n";

							for (ErrorMessage errorMessage : errors) {
								generalErrorMessage += "* "
										+ errorMessage.getMessage() + "\n";
							}

							JOptionPane.showMessageDialog(null,
									generalErrorMessage, "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"No hay entidades seleccionadas.",
								"Exportar datos", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		};
	}

	/**
	 * Absolute path (on Windows) should be created as:
	 * [path]\\[entityname]_[time].csv
	 * 
	 * @param entities
	 *            List of selected entities to be exported.
	 * @param path
	 *            Destination folder path.
	 * @return Simple {@link Response} object
	 */
	private Response exportDataFor(List<String> entities, String path) {
		Response response = new Response();

		for (String entity : entities) {
			String absolutePath = String.format("%s%s_%s.csv",
					Utilities.duplicateCharacter('\\', path), entity,
					Utilities.getCurrentDateWithoutChars());

			Response partialResponse = ConfigurationService.exportEntity(
					entity, absolutePath);

			if (!partialResponse.wasSuccessful()) {
				response.addError(partialResponse.getErrorsMessages().get(0));
			}
		}

		return response;
	}
}

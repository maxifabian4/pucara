package com.pucara.controller.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.response.Response;
import com.pucara.core.services.sale.SaleService;
import com.pucara.view.main.MainViewOne;

public class MainControllerOne {
	private MainViewOne mainView;

	public MainControllerOne(MainViewOne mainView) {
		this.mainView = mainView;

//		MySqlAccess.establishConection();
		mainView.createNewTextField(getActionListenerInput());
	}

	public void displayView() {
		mainView.displayComponents();
	}

	private ActionListener getActionListenerInput() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Get information from view.
				String input = mainView.getInputValue();
				addProductToPartialList(input);
			}
		};
	}

	private void addProductToPartialList(String inputBarcodeText) {
		if (inputBarcodeText.equals(CommonData.EMPTY_STRING)) {
			JOptionPane.showConfirmDialog(null,
					CommonMessageError.BARCODE_DESCRIPTION_NO_DETECTED,
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		} else {
			Response response = SaleService.addProductToList(inputBarcodeText);

			if (response.wasSuccessful()) {
				mainView.addPartialListToPanel(SaleService.getPartialList()
						.toArray());
			}
		}
	}
}

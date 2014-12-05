package com.pucara.controller.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.common.SaleSummaryPanel;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.response.Response;
import com.pucara.core.services.product.ProductService;
import com.pucara.core.services.sale.SaleService;
import com.pucara.view.main.MainViewOne;

public class MainControllerOne {
	private MainViewOne mainView;

	public MainControllerOne(MainViewOne mainView) {
		this.mainView = mainView;

		// Must be removed, since all db access is migrated to Liquibase ...
		MySqlAccess.establishConection();

		mainView.createNewTextField();

		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (ClassNotFoundException | InstantiationException
		// | IllegalAccessException | UnsupportedLookAndFeelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		setupAutoComplete(mainView.getInputTextField(),
				ProductService.getAllDescriptions());
	}

	public void displayView() {
		mainView.displayComponents();
	}

	private void addProductToPartialList(String inputBarcodeText) {
		if (inputBarcodeText.equals(CommonData.EMPTY_STRING)) {
			JOptionPane.showConfirmDialog(null,
					CommonMessageError.BARCODE_DESCRIPTION_NO_DETECTED,
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		} else {
			Response response = SaleService.addProductToList(inputBarcodeText);

			if (!response.wasSuccessful()) {
				JOptionPane.showConfirmDialog(null, response
						.getErrorsMessages().get(0).getMessage(),
						"Advertencia", JOptionPane.WARNING_MESSAGE);
			} else {
				Object[] products = SaleService.getPartialList().toArray();
				mainView.addPartialListToPanel(products);
				mainView.cleanInputTextField();

				if (mainView.alreadyExistSummary()) {
					mainView.updateSummary(products);
				} else {
					mainView.createSummary(products);
				}
			}
		}
	}

	/**
	 * Implements all auto-complete logic.
	 * 
	 * @param txtInput
	 * @param items
	 */
	private void setupAutoComplete(final JTextField txtInput,
			final List<String> items) {
		final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

		/**
		 * Style and listeners for the combo box.
		 */
		final JComboBox<?> cbInput = new JComboBox<String>(model) {
			private static final long serialVersionUID = 1L;

			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};

		cbInput.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 17));

		setAdjusting(cbInput, false);

		for (String item : items) {
			model.addElement(item);
		}

		cbInput.setSelectedItem(null);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
					}
				}
			}
		});

		/**
		 * Style and listeners for the combo box.
		 */
		txtInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(cbInput, true);

				if (e.getKeyCode() == KeyEvent.VK_ENTER
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);

					if (e.getKeyCode() == KeyEvent.VK_ENTER
							&& cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
						addProductToPartialList(ProductService
								.getBarcodeByDescription(cbInput
										.getSelectedItem().toString()));
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}

				setAdjusting(cbInput, false);
			}
		});

		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				setAdjusting(cbInput, true);
				model.removeAllElements();
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					for (String item : items) {
						if (item.toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(item);
						}
					}
				}
				cbInput.setPopupVisible(model.getSize() > 0);
				setAdjusting(cbInput, false);
			}
		});

		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}

	private boolean isAdjusting(JComboBox<?> cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}

	private void setAdjusting(JComboBox<?> cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}

}

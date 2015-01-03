package com.pucara.controller.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.SystemPopup;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.generic.Utilities;
import com.pucara.core.response.Response;
import com.pucara.core.services.product.ProductService;
import com.pucara.core.services.sale.SaleService;
import com.pucara.view.main.MainViewOne;

public class MainControllerOne {
	private MainViewOne mainView;

	public MainControllerOne(MainViewOne mainView) {
		this.mainView = mainView;

		// Must be removed, since all db access is migrated to Liquibase.
		MySqlAccess.establishConection();

		// Create general input text field and listener.
		mainView.createNewTextField();
		setupAutoComplete(mainView.getInputTextField(),
				ProductService.getAllDescriptions());

		// Create list for partial products.
		mainView.createPartialList(generateListKeyListener());
	}

	public void displayView() {
		mainView.displayComponents();
	}

	private KeyListener generateListKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				String barcode = mainView.getSelectedProduct();

				if (e.getID() == KeyEvent.KEY_PRESSED
						&& !barcode.equals(CommonData.EMPTY_STRING)) {
					if (e.getKeyCode() == KeyEvent.VK_MINUS) {
						int numberBeforeChange = SaleService
								.getNumberOfDistinctProducts();

						SaleService.decreaseRequiredProduct(barcode);

						int numberAfterChange = SaleService
								.getNumberOfDistinctProducts();
						mainView.updatePartialElements(SaleService
								.getPartialList().toArray());

						if (numberAfterChange == numberBeforeChange) {
							mainView.selectPartialElementByBarcode(barcode);
						} else if (numberAfterChange < numberBeforeChange
								&& numberAfterChange != 0) {
							mainView.selectPartialElement(CommonData.FIRST_ROW);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						SaleService.removeProductFromList(barcode);
						mainView.updatePartialElements(SaleService
								.getPartialList().toArray());

						if (SaleService.getTotalNumberOfProducts() > 0) {
							mainView.selectPartialElement(CommonData.FIRST_ROW);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ADD) {
						// if (!barcode.contains("@")) {
						addProductToPartialList(barcode);
						mainView.selectPartialElementByBarcode(barcode);
						// }
					} else if (e.getKeyCode() == KeyEvent.VK_F5
							&& SaleService.getTotalNumberOfProducts() > 0) {
						SystemPopup popup = new SystemPopup(
								CommonUIComponents
										.createInformationPanel("Desea confirmar la venta?"),
								"Confirmación", SystemPopup.CONFIRMATION);
						popup.addMouseListener(createMouseListenerForSale(popup));
						popup.addKeyListener(createKeyListenerForSale(popup));
						popup.setVisible(true);
					} else if (e.getKeyCode() == KeyEvent.VK_F6) {
						JPanel contentPane = CommonUIComponents
								.createPanelForProductQuantity(
										"Cantidad de productos");

						SystemPopup popup = new SystemPopup(contentPane,
								"Ingreso de datos", SystemPopup.CONFIRMATION);

						JTextField textField = (JTextField) contentPane
								.getClientProperty(CommonData.PROPERTY_QUANTITY);

						popup.addMouseListener(createMouseListenerForQuantity(
								popup, barcode, textField));
						popup.setVisible(true);
					}
				}
			}
		};
	}

	private KeyListener createKeyListenerForSale(final SystemPopup popup) {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Response performedSale = SaleService.makeASale();

					if (performedSale.wasSuccessful()) {
						SaleService.cleanPartialList();
						mainView.updatePartialElements(null);
						popup.dispose();
					} else {
						// TODO Show a message with the error !!!!
					}
				}
			}
		};
	}

	private MouseListener createMouseListenerForSale(final SystemPopup popup) {
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
				popup.setCursorForLabel(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				Response performedSale = SaleService.makeASale();

				if (performedSale.wasSuccessful()) {
					SaleService.cleanPartialList();
					mainView.updatePartialElements(null);
					popup.dispose();
				}
			}
		};
	}

	private MouseListener createMouseListenerForQuantity(
			final SystemPopup popup, final String barcode,
			final JTextField textField) {
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
				popup.setCursorForLabel(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				Integer n = Utilities.getIntegerValue(textField.getText());

				if (n != null) {
					Response result = SaleService.increaseRequiredProduct(
							barcode, n);

					if (result.wasSuccessful()) {
						mainView.updatePartialElements(SaleService
								.getPartialList().toArray());
						mainView.selectPartialElementByBarcode(barcode);
						popup.dispose();
					} else {
						mainView.showError(result.getErrorsMessages().get(0)
								.getMessage());
					}
				} else {
					popup.dispose();
				}
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
				if (e.getKeyCode() == KeyEvent.VK_F5
						&& !SaleService.getPartialList().isEmpty()) {
					SystemPopup popup = new SystemPopup(
							CommonUIComponents
									.createInformationPanel("Desea confirmar la venta?"),
							"Confirmación", SystemPopup.CONFIRMATION);
					popup.addMouseListener(createMouseListenerForSale(popup));
					popup.addKeyListener(createKeyListenerForSale(popup));
					popup.setVisible(true);
				} else {
					setAdjusting(cbInput, true);

					if (e.getKeyCode() == KeyEvent.VK_ENTER
							|| e.getKeyCode() == KeyEvent.VK_UP
							|| e.getKeyCode() == KeyEvent.VK_DOWN) {
						e.setSource(cbInput);
						cbInput.dispatchEvent(e);

						if (e.getKeyCode() == KeyEvent.VK_ENTER
								&& cbInput.getSelectedItem() != null) {
							txtInput.setText(cbInput.getSelectedItem()
									.toString());
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

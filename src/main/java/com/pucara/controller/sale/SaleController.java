package com.pucara.controller.sale;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.controller.observable.UpdatesSource;
import com.pucara.core.entities.PartialElement;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.Response;
import com.pucara.core.services.product.ProductService;
import com.pucara.core.services.sale.SaleService;
import com.pucara.view.sale.SaleView;

/**
 * This class represents the Category controller in the system.
 * 
 * @author Maximiliano Fabian
 */
public class SaleController implements Observer {
	private SaleView saleView;
	private UpdatesSource subject;

	public SaleController(SaleView saleView, UpdatesSource subject) {
		this.saleView = saleView;
		this.subject = subject;
		SaleService.cleanPartialList();
	}

	/**
	 * 
	 */
	public void addProductToPartialList(String inputBarcodeText) {
		if (inputBarcodeText.equals(CommonData.EMPTY_STRING)) {
			JOptionPane.showConfirmDialog(null,
					CommonMessageError.BARCODE_DESCRIPTION_NO_DETECTED,
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		} else {
			Response response = SaleService.addProductToList(inputBarcodeText);

			if (response.wasSuccessful()) {
				saleView.addPartialListToPanel(SaleService.getPartialList()
						.toArray());
				saleView.cleanInputTextField();
			} else {
				JOptionPane.showMessageDialog(null, response
						.getErrorsMessages().get(0).getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * @return KeyListener
	 */
	public KeyListener createInputKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				int caracter = e.getKeyChar();
				String text = saleView.getBarcodeFromTextField();

				if ((caracter <= 'z' && caracter >= 'a')
						&& (caracter != '' && caracter != '')
						|| caracter == ' ') {
					text += e.getKeyChar();
				}

				if (!text.isEmpty() && !Utilities.areAllDigits(text)) {
					ProductListResponse descResponse = ProductService
							.existsProduct(new SearchProductRequest(null, text));

					if (descResponse.wasSuccessful()) {
						saleView.addPotentialProductsOnList(descResponse
								.getProducts().toArray());
					} else {
						saleView.addPotentialProductsOnList(new Object[] {});
					}
				} else if (text.isEmpty()) {
					saleView.addPotentialProductsOnList(new Object[] {});
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addProductToPartialList(saleView.getBarcodeFromTextField());
				} else if (e.getKeyCode() == KeyEvent.VK_F5
						&& SaleService.getTotalNumberOfProducts() > 0) {
					Object[] options = { "Si", "No" };
					int response = JOptionPane.showOptionDialog(null,
							"Desea completar la venta?", "Confirmar venta ...",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[0]);

					if (response == JOptionPane.YES_OPTION) {
						List<PartialElement> elements = SaleService
								.getPartialList();
						Response performedSale = SaleService.makeASale();

						if (performedSale.wasSuccessful()) {
							subject.catchUpdate(elements);
							cleanPartialList();
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN
						&& saleView.isPotentialListPresent()) {
					saleView.selectPotentialElement(0);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
	}

	/**
	 * 
	 */
	public void cleanPartialList() {
		SaleService.cleanPartialList();
		saleView.updatePartialElements(null);
	}

	public KeyListener generateListKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					String barcode = saleView.getSelectedProduct();
					if (e.getKeyCode() == KeyEvent.VK_MINUS) {
						if (!saleView.isFocusOnTextField()) {
							int numberBeforeChange = SaleService
									.getNumberOfDistinctProducts();

							SaleService.decreaseRequiredProduct(barcode);

							int numberAfterChange = SaleService
									.getNumberOfDistinctProducts();
							saleView.updatePartialElements(SaleService
									.getPartialList().toArray());

							if (numberAfterChange == numberBeforeChange) {
								saleView.selectPartialElementByBarcode(barcode);
							} else if (numberAfterChange < numberBeforeChange
									&& numberAfterChange != 0) {
								saleView.cleanListSelection();
								saleView.selectPartialElement(CommonData.FIRST_ROW);
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						SaleService.removeProductFromList(barcode);
						saleView.updatePartialElements(SaleService
								.getPartialList().toArray());

						if (SaleService.getTotalNumberOfProducts() > 0) {
							saleView.selectPartialElement(CommonData.FIRST_ROW);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ADD) {
						if (!barcode.contains("@")) {
							addProductToPartialList(barcode);
							saleView.selectPartialElementByBarcode(barcode);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F5
							&& SaleService.getTotalNumberOfProducts() > 0) {
						Object[] options = { "Si", "No" };
						int response = JOptionPane.showOptionDialog(null,
								"Desea completar la venta?",
								"Confirmar venta ...",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE, null, options,
								options[0]);

						if (response == JOptionPane.YES_OPTION) {
							Response performedSale = SaleService.makeASale();

							if (performedSale.wasSuccessful()) {
								cleanPartialList();
							} else {
								// TODO Show a message with the error !!!!
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_F6) {
						String[] options = { "OK" };
						JPanel panel = new JPanel();
						JLabel lbl = new JLabel("cantidad de productos: ");
						JTextField txt = new JTextField("100");
						panel.add(lbl);
						panel.add(txt);
						int selectedOption = JOptionPane.showOptionDialog(null,
								panel, "Ingresar catidad de productos",
								JOptionPane.NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);

						if (selectedOption == 0) {
							Integer n = Utilities
									.getIntegerValue(txt.getText());

							if (n != null) {
								SaleService.increaseRequiredProduct(barcode, n);
								saleView.updatePartialElements(SaleService
										.getPartialList().toArray());
								saleView.selectPartialElementByBarcode(barcode);
							}
						}
					}
				}
			}
		};
	}

	public KeyListener generateListPotentialKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addProductToPartialList(saleView.getPotentialItem());
					saleView.addPotentialProductsOnList(new Object[] {});
					saleView.setFocusOnInput();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
	}

	public MouseListener createPotentialMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					addProductToPartialList(saleView.getPotentialItem());
					saleView.addPotentialProductsOnList(new Object[] {});
					saleView.setFocusOnInput();
				}
			}
		};
	}

	@Override
	public void update(Observable o, Object arg) {
		SaleService.updateProductFromList((String) arg);

		Object[] products = SaleService.getPartialList().toArray();
		if (SaleService.getPartialList().toArray() != null
				&& products.length != 0) {
			saleView.updatePartialElements(products);
		}
	}

}

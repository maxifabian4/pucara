package com.pucara.controller.sale;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.common.CustomLogger;
import com.pucara.common.CustomLogger.LoggerLevel;
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
public class SaleController {
	private SaleView saleView;

	public SaleController(SaleView saleView) {
		this.saleView = saleView;
		SaleService.cleanPartialList();
	}

	/**
	 * 
	 */
	public void addProductToPartialList(String inputBarcodeText) {
		if (inputBarcodeText.equals(CommonData.EMPTY_STRING)) {
			JOptionPane.showConfirmDialog(null, CommonMessageError.BARCODE_DESCRIPTION_NO_DETECTED,
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		} else {
			Response response = SaleService.addProductToList(inputBarcodeText);

			if (response.wasSuccessful()) {
				saleView.addPartialListToPanel(SaleService.getPartialList().toArray());
			} else {
				JOptionPane.showMessageDialog(null, response.getErrorsMessages().get(0)
						.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

			saleView.cleanInputTextField();
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

				if ((caracter <= 'z' && caracter >= 'a') && (caracter != '' && caracter != '')
						|| caracter == ' ') {
					text += e.getKeyChar();
				}

				if (!text.isEmpty() && !Utilities.areAllDigits(text)) {
					ProductListResponse descResponse = ProductService
							.existsProduct(new SearchProductRequest(null, text));

					if (descResponse.wasSuccessful()) {
						saleView.addPotentialProductsOnList(descResponse.getProducts().toArray());
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
					int response = JOptionPane.showOptionDialog(null, "Desea completar la venta?",
							"Confirmar venta ...", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options, options[0]);

					if (response == JOptionPane.YES_OPTION) {
						Response performedSale = SaleService.makeASale();

						if (performedSale.wasSuccessful()) {
							cleanPartialList();
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && saleView.isPotentialListPresent()) {
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
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						if (!saleView.isFocusOnTextField()) {
							String barcode = saleView.getSelectedProduct();
							int numberBeforeChange = SaleService.getNumberOfDistinctProducts();

							SaleService.decreaseRequiredProduct(barcode);

							int numberAfterChange = SaleService.getNumberOfDistinctProducts();
							saleView.updatePartialElements(SaleService.getPartialList().toArray());

							if (numberAfterChange == numberBeforeChange) {
								saleView.selectPartialElementByBarcode(barcode);
							} else if (numberAfterChange < numberBeforeChange
									&& numberAfterChange != 0) {
								saleView.cleanListSelection();
								saleView.selectPartialElement(CommonData.FIRST_ROW);
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ADD) {
						String barcode = saleView.getSelectedProduct();
						addProductToPartialList(barcode);
						saleView.selectPartialElementByBarcode(barcode);
					} else if (e.getKeyCode() == KeyEvent.VK_F5
							&& SaleService.getTotalNumberOfProducts() > 0) {
						Object[] options = { "Si", "No" };
						int response = JOptionPane.showOptionDialog(null,
								"Desea completar la venta?", "Confirmar venta ...",
								JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
								options, options[0]);

						if (response == JOptionPane.YES_OPTION) {
							Response performedSale = SaleService.makeASale();

							if (performedSale.wasSuccessful()) {
								cleanPartialList();
							} else {
								CustomLogger.log(LoggerLevel.ERROR, performedSale
										.getErrorsMessages().get(0).getMessage());
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

}
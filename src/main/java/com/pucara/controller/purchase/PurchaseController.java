package com.pucara.controller.purchase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;

import com.pucara.view.purchase.PurchaseView;
import com.pucara.view.render.object.ListSalePotentialProduct;
import com.pucara.view.render.object.ListSaleProduct;
import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.common.CustomLogger;
import com.pucara.common.CustomLogger.LoggerLevel;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.request.UpdateProductRequest;
import com.pucara.core.request.VerifyProductValuesRequest;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.ProductResponse;
import com.pucara.core.response.Response;
import com.pucara.core.services.product.ProductService;
import com.pucara.core.services.transaction.PurchaseService;

/**
 * 
 * @author pucara
 */
public class PurchaseController {
	private PurchaseService purchaseService;
	private PurchaseView purchaseView;

	public PurchaseController(PurchaseView purchaseView) {
		purchaseService = new PurchaseService();
		this.purchaseView = purchaseView;
	}

	public ActionListener createNewPurchaseListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> formValues = purchaseView.getFormValues();

				if (!areInputsCorrect(formValues.get(0), formValues.get(1))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Descripción o valor del gasto son inválidos. Ingrese nuevamente los valores.",
									"Error", JOptionPane.INFORMATION_MESSAGE);
				} else {
					purchaseService.setPurchaseDescription(formValues.get(0));
					double expense = purchaseService.getTotalValue();

					if (expense == 0) {
						purchaseService.setPurchaseExpense(formValues.get(1));
					} else {
						purchaseService.setPurchaseExpense(String.valueOf(expense));
					}

					Response performedPurchase = purchaseService.performTransaction();

					if (performedPurchase.wasSuccessful()) {
						cleanPartialList();
						purchaseView.cleanPurchaseTextFields();
						JOptionPane.showMessageDialog(null, "Gasto ingresado con éxito.",
								"Información", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, performedPurchase.getErrorsMessages()
								.get(0).getMessage(), "Atención", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		};
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createPartialListMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				purchaseView.selectInputTextField();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
	}

	/**
	 * 
	 */
	public void cleanPartialList() {
		purchaseService.cleanPartialList();
		purchaseView.updatePartialElements(new ListSaleProduct[] {});
		purchaseView.selectInputTextField();
	}

	/**
	 * 
	 * @return List of rows.
	 */
	public Object[] generatePartialProductRows() {
		return purchaseService.getPartialList().toArray();
	}

	public KeyListener createInputKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				int caracter = e.getKeyChar();
				String text = purchaseView.getBarcodeFromTextField();

				if ((caracter <= 'z' && caracter >= 'a') && (caracter != '' && caracter != '')
						|| caracter == ' ') {
					text += e.getKeyChar();
				}

				if (!text.isEmpty() && !Utilities.areAllDigits(text)) {
					ProductListResponse descResponse = ProductService
							.existsProduct(new SearchProductRequest(null, text));

					if (descResponse.wasSuccessful()) {
						purchaseView.addPotentialProductsOnList(descResponse.getProducts()
								.toArray());
					} else {
						purchaseView.addPotentialProductsOnList(new ListSalePotentialProduct[] {});
					}
				} else if (text.isEmpty()) {
					purchaseView.addPotentialProductsOnList(new ListSalePotentialProduct[] {});
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addProductToPartialList(purchaseView.getBarcodeFromTextField());
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN
						&& purchaseView.isPotentialListPresent()) {
					purchaseView.selectPotentialElement(0);
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
					addProductToPartialList(purchaseView.getPotentialItem());
					purchaseView.addPotentialProductsOnList(new ListSalePotentialProduct[] {});
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
					addProductToPartialList(purchaseView.getPotentialItem());
					purchaseView.addPotentialProductsOnList(new ListSalePotentialProduct[] {});
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
	}

	/**
	 * 
	 * @return ActionListener
	 */
	public ActionListener createUpdateProductListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Retrieve information from the stock view.
				List<String> popupValues = purchaseView.getPopupValues();

				String description = popupValues.get(0);
				String cost = popupValues.get(1);
				String percentage = popupValues.get(2);
				String minStock = popupValues.get(3);
				String barcode = purchaseView.getSelectedPurchaseBarcode();

				VerifyProductValuesRequest productValuesRequest = new VerifyProductValuesRequest(
						barcode, description, cost, percentage, "0", minStock, null);

				ProductResponse validatedProductResponse = ProductService
						.getValidatedProduct(productValuesRequest);

				if (validatedProductResponse.wasSuccessful()) {
					// Call the service.
					ProductResponse response = ProductService
							.updateProduct(new UpdateProductRequest(validatedProductResponse
									.getProduct()));

					if (response.wasSuccessful()) {
						purchaseView.closeUpdatePopup();
						// Update partial list.
						purchaseService.updatePartialElement(barcode, description, cost,
								percentage, minStock);
						purchaseView.updateProductsList();
						purchaseView.selectProductElementOnList(barcode);
						CustomLogger.log(null, LoggerLevel.INFO, String.format(
								"Product [%s] has been modified successfully.", description));
					} else {
						JOptionPane.showMessageDialog(null, "No se ha modificado el producto ...",
								"Advertencia!", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					purchaseView.closeUpdatePopup();
					JOptionPane.showMessageDialog(null, validatedProductResponse
							.getErrorsMessages().get(0).getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
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
						if (!purchaseView.isFocusOnTextField()) {
							String barcode = purchaseView.getSelectedProduct();
							int numberBeforeChange = purchaseService.getNumberOfDistinctProducts();

							purchaseService.decreaseRequiredProduct(barcode);

							int numberAfterChange = purchaseService.getNumberOfDistinctProducts();
							purchaseView.updatePartialElements(purchaseService.getPartialList()
									.toArray());
							purchaseView.updateCostField(purchaseService.getTotalValue());

							if (numberAfterChange == numberBeforeChange) {
								purchaseView.selectPartialElementByBarcode(barcode);
							} else if (numberAfterChange < numberBeforeChange
									&& numberAfterChange != 0) {
								purchaseView.cleanListSelection();
								purchaseView.selectPartialElement(CommonData.FIRST_ROW);
							} else if (numberAfterChange == 0) {
								purchaseService.cleanPartialList();
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ADD) {
						String barcode = purchaseView.getSelectedProduct();
						addProductToPartialList(barcode);
						purchaseView.selectPartialElementByBarcode(barcode);
					}
				}
			}
		};
	}

	public void addProductToPartialList(String inputBarcodeText) {
		if (inputBarcodeText.equals(CommonData.EMPTY_STRING)) {
			JOptionPane.showConfirmDialog(null, CommonMessageError.BARCODE_DESCRIPTION_NO_DETECTED,
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		} else {
			Response response = purchaseService.addProductToList(inputBarcodeText);

			if (response.wasSuccessful()) {
				purchaseView.addPartialListToPanel(purchaseService.getPartialList().toArray());
				purchaseView.updateCostField(purchaseService.getTotalValue());
			} else {
				JOptionPane.showMessageDialog(null, response.getErrorsMessages().get(0)
						.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

			purchaseView.cleanInputTextField();
		}
	}

	/**
	 * 
	 * @return KeyListener
	 */
	public KeyListener createKeyListener() {
		return new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// if enter button is pressed in keyboard, then show
				// "Enter Button pressed" message
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					purchaseView.closeUpdatePopup();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// To change body of implemented methods use File | Settings |
				// File Templates.
			}
		};
	}

	public MouseListener generateListMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					purchaseView.displayPopup();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
	}

	/**
	 * 
	 * @param description
	 * @param expense
	 * @return
	 */
	private boolean areInputsCorrect(String description, String expense) {
		return description != null && expense != null
				&& !description.trim().equals(CommonData.EMPTY_STRING)
				&& !expense.trim().equals(CommonData.EMPTY_STRING)
				&& Utilities.getDoubleValue(expense) != null
				&& Utilities.getDoubleValue(expense) > 0;
	}

}
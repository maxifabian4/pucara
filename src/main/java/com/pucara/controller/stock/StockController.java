package com.pucara.controller.stock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import com.pucara.controller.observable.UpdatesSource;
import com.pucara.core.entities.Product;
import com.pucara.core.request.NewProductRequest;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.request.UpdateProductRequest;
import com.pucara.core.request.VerifyProductValuesRequest;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.ProductResponse;
import com.pucara.core.services.product.ProductService;
import com.pucara.view.stock.StockView;

/**
 * This class represents the Stock controller in the system.
 * 
 * @author Maximiliano
 */
public class StockController implements Observer {
	private StockView stockView;
	private UpdatesSource subject;

	public StockController(StockView stockView, UpdatesSource subject) {
		this.stockView = stockView;
		this.subject = subject;
	}

	/**
	 * Creates a new listener in order to create a new product.
	 * 
	 * @return ActionListener
	 */
	public ActionListener createNewProductListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VerifyProductValuesRequest productValuesRequest = createNewRequest();

				ProductResponse validatedProductResponse = ProductService
						.getValidatedProduct(productValuesRequest);

				if (validatedProductResponse.wasSuccessful()) {
					Product product = validatedProductResponse.getProduct();
					product.setByPercentage(stockView.getNewByPercentage());
					ProductResponse newProductResponse = ProductService
							.addProduct(new NewProductRequest(product));

					if (!newProductResponse.wasSuccessful()) {
						JOptionPane.showMessageDialog(null, newProductResponse
								.getErrorsMessages().get(0).getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						// It takes more than 2 seconds trying to update the
						// partial list, when more than 100 products are stored.
						stockView.updateProductsList();
						stockView.cleanAllForm();
						stockView.setFocusOnInput();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							validatedProductResponse.getErrorsMessages().get(0)
									.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	/**
	 * Creates a new listener in order to search an existing product.
	 * 
	 * @return ActionListener
	 */
	public ActionListener createSearchProductListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String productFromView = stockView.getProductToFind()
						.toLowerCase();

				ProductListResponse barcodeResponse = ProductService
						.existsProduct(new SearchProductRequest(
								productFromView, null));

				if (barcodeResponse.wasSuccessful()) {
					stockView.selectProductElementOnList(productFromView);
					stockView.cleanSearchField();
				} else {
					ProductListResponse descResponse = ProductService
							.existsProduct(new SearchProductRequest(null,
									productFromView));

					if (descResponse.wasSuccessful()) {
						stockView.selectProductElementsOnList(descResponse
								.getProducts());
					} else {
						JOptionPane.showMessageDialog(null, descResponse
								.getErrorsMessages().get(0).getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
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
				VerifyProductValuesRequest productValuesRequest = createRequestForUpdate();

				ProductResponse validatedProductResponse = ProductService
						.getValidatedProduct(productValuesRequest);

				if (validatedProductResponse.wasSuccessful()) {
					Product product = validatedProductResponse.getProduct();
					product.setByPercentage(stockView.isByPercentage());
					// Call the service.
					ProductResponse response = ProductService
							.updateProduct(new UpdateProductRequest(
									validatedProductResponse.getProduct()));

					if (response.wasSuccessful()) {
						stockView.closeUpdatePopup();
						stockView.updateProductsList();
						stockView
								.selectProductElementOnList(productValuesRequest
										.getBarcode());
						subject.catchUpdate(productValuesRequest.getBarcode());
					} else {
						JOptionPane.showMessageDialog(null,
								"No se ha modificado el producto ...",
								"Advertencia!", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					stockView.closeUpdatePopup();
					JOptionPane.showMessageDialog(null,
							validatedProductResponse.getErrorsMessages().get(0)
									.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
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
					stockView.closeUpdatePopup();
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
					stockView.displayPopup();
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

	public ActionListener createCostListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stockView.changeTextFields();
			}
		};
	}

	@Override
	public void update(Observable o, Object arg) {
		stockView.updateProductsList();
	}

	private VerifyProductValuesRequest createNewRequest() {
		if (stockView.isByPercentage()) {
			return new VerifyProductValuesRequest(stockView.getProductBarcode()
					.toLowerCase(), stockView.getProductDescription()
					.toLowerCase(), stockView.getInitialCost(), "0.0",
					stockView.getProductPercentage(), "0",
					stockView.getProductStockMin(),
					stockView.getProductCategory());
		} else {
			String initialCost = stockView.getInitialCost();
			String finalCost = stockView.getFinalCost();

			return new VerifyProductValuesRequest(stockView.getProductBarcode()
					.toLowerCase(), stockView.getProductDescription()
					.toLowerCase(), initialCost, finalCost, "0.0", "0",
					stockView.getProductStockMin(),
					stockView.getProductCategory());
		}
	}

	private VerifyProductValuesRequest createRequestForUpdate() {
		// Retrieve information from the stock view.
		List<String> popupComponents = stockView.getPopupComponents();
		String description, initialCost, finalCost, percentage, minStock, barcode;

		if (stockView.isByPercentage()) {
			description = popupComponents.get(0);
			initialCost = popupComponents.get(1);
			percentage = popupComponents.get(2);
			minStock = popupComponents.get(3);
			barcode = stockView.getSelectedBarcode();

			return new VerifyProductValuesRequest(barcode, description,
					initialCost, "0.0", percentage, "0", minStock, null);
		} else {
			description = popupComponents.get(0);
			initialCost = popupComponents.get(1);
			finalCost = popupComponents.get(2);
			minStock = popupComponents.get(3);
			barcode = stockView.getSelectedBarcode();

			return new VerifyProductValuesRequest(barcode, description,
					initialCost, finalCost, "0.0", "0", minStock, null);
		}
	}

}

package com.pucara.controller.stock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;

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
public class StockController {

	private StockView stockView;

	public StockController(StockView stockView) {
		this.stockView = stockView;
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
				VerifyProductValuesRequest productValuesRequest = new VerifyProductValuesRequest(
						stockView.getProductBarcode().toLowerCase(), stockView
								.getProductDescription().toLowerCase(), stockView.getProductCost(),
						stockView.getProductPercentage(), "0", stockView.getProductStockMin(),
						stockView.getProductCategory());

				ProductResponse validatedProductResponse = ProductService
						.getValidatedProduct(productValuesRequest);

				if (validatedProductResponse.wasSuccessful()) {
					ProductResponse newProductResponse = ProductService
							.addProduct(new NewProductRequest(validatedProductResponse.getProduct()));

					if (!newProductResponse.wasSuccessful()) {
						JOptionPane.showMessageDialog(null, newProductResponse.getErrorsMessages()
								.get(0).getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						// It takes more than 2 seconds trying to update the
						// partial list, when more than 100 products are stored.
						stockView.updateProductsList();
						// stockView.selectProductElementOnList(productValuesRequest.getBarcode());
						stockView.cleanAllForm();
						stockView.setFocusOnInput();
					}
				} else {
					JOptionPane.showMessageDialog(null, validatedProductResponse
							.getErrorsMessages().get(0).getMessage(), "Error",
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
				String productFromView = stockView.getProductToFind().toLowerCase();

				ProductListResponse barcodeResponse = ProductService
						.existsProduct(new SearchProductRequest(productFromView, null));

				if (barcodeResponse.wasSuccessful()) {
					stockView.selectProductElementOnList(productFromView);
					stockView.cleanSearchField();
				} else {
					ProductListResponse descResponse = ProductService
							.existsProduct(new SearchProductRequest(null, productFromView));

					if (descResponse.wasSuccessful()) {
						stockView.selectProductElementsOnList(descResponse.getProducts());
					} else {
						JOptionPane.showMessageDialog(null, descResponse.getErrorsMessages().get(0)
								.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
				// Retrieve information from the stock view.
				List<String> popupComponents = stockView.getPopupComponents();

				String description = popupComponents.get(0);
				String cost = popupComponents.get(1);
				String percentage = popupComponents.get(2);
				String minStock = popupComponents.get(3);
				String barcode = stockView.getSelectedBarcode();

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
						stockView.closeUpdatePopup();
						stockView.updateProductsList();
						stockView.selectProductElementOnList(barcode);
						// CustomLogger.log(null, LoggerLevel.INFO,
						// String.format(
						// "Product [%s] has been modified successfully.",
						// description));
					} else {
						JOptionPane.showMessageDialog(null, "No se ha modificado el producto ...",
								"Advertencia!", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					stockView.closeUpdatePopup();
					JOptionPane.showMessageDialog(null, validatedProductResponse
							.getErrorsMessages().get(0).getMessage(), "Error",
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

}

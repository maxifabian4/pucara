package com.pucara.controller.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.pucara.controller.configuration.ConfigurationController;
import com.pucara.controller.observable.UpdatesSource;
import com.pucara.controller.purchase.PurchaseController;
import com.pucara.controller.report.ReportController;
import com.pucara.controller.sale.SaleController;
import com.pucara.controller.stock.StockController;
import com.pucara.view.configuration.ConfigurationView;
import com.pucara.view.main.HeaderView;
import com.pucara.view.main.MainView;
import com.pucara.view.purchase.PurchaseView;
import com.pucara.view.report.ReportView;
import com.pucara.view.sale.SaleView;
import com.pucara.view.stock.StockView;

/**
 * REMOVE !!
 * 
 * This class represents the Header controller in the system. 
 * 
 * @author Maximiliano
 */
public class HeaderController {
	// Maintains a list of its dependents, called observers, and notifies them
	// automatically of any state changes.
	private UpdatesSource subject;

	// Header view.
	private HeaderView headerView;
	// Main view.
	private MainView mainView;

	// Purchase view.
	private PurchaseView purchaseView;
	// Purchase controller.
	private PurchaseController purchaseController;
	// Purchase model.
	// private PurchaseService purchaseService;

	// Sale view.
	private SaleView saleView;
	// Sale controller.
	private SaleController saleController;

	// Stock view.
	private StockView stockView;
	// Stock controller.
	private StockController stockController;

	// Configuration view.
	private ConfigurationView configurationView;
	// Configuration controller.
	private ConfigurationController configurationController;

	public HeaderController(HeaderView headerView, MainView mainView) {
		this.subject = new UpdatesSource();
		this.headerView = headerView;
		this.mainView = mainView;

		this.purchaseView = new PurchaseView(this.subject);
		this.saleView = new SaleView(this.subject);
		this.stockView = new StockView(this.subject);

		this.configurationView = new ConfigurationView();
		this.configurationController = new ConfigurationController(
				configurationView);

		// Update updated information product.
		mainView.addNewCentralPanel(saleView);
		// Apply view properties.
		saleView.repaint();
		saleView.setFocusOnInput();
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createStockListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Remove existing central panel.
				mainView.removeCentralPanel();
				// Update updated information product.
				mainView.addNewCentralPanel(stockView);
				// Apply view properties.
				stockView.repaint();
				headerView.changeToBold(headerView.getStockLabel());
				stockView.setFocusOnInput();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getStockLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}
		};
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createSaleListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Remove existing central panel.
				mainView.removeCentralPanel();
				// Update updated information product.
				mainView.addNewCentralPanel(saleView);
				// Apply view properties.
				saleView.repaint();
				headerView.changeToBold(headerView.getSaleLabel());
				saleView.setFocusOnInput();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getSaleLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}
		};
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createCloseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainView.closeApplication();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getCloseLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

		};
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createReportsListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Remove existing central panel.
				mainView.removeCentralPanel();

				ReportView view = new ReportView();
				ReportController controller = new ReportController(view);

				// Update updated information product.
				mainView.addNewCentralPanel(view);
				// Apply view properties.
				headerView.changeToBold(headerView.getReportsLabel());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getReportsLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}
		};
	}

	public MouseListener createConfigurationListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Remove existing central panel.
				mainView.removeCentralPanel();
				// Update updated information product.
				mainView.addNewCentralPanel(configurationView);
				configurationView.repaint();
				// Apply view properties.
				headerView.changeToBold(headerView.getConfigurationLabel());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView
						.changeCursorLabel(headerView.getConfigurationLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}
		};
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createPurchaseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Remove existing central panel.
				mainView.removeCentralPanel();
				// Update updated information product.
				mainView.addNewCentralPanel(purchaseView);
				// Apply view properties.
				purchaseView.repaint();
				headerView.changeToBold(headerView.getPurchaseLabel());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getPurchaseLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}
		};
	}

}

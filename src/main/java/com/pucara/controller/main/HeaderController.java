package com.pucara.controller.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.pucara.view.category.CategoryView;
import com.pucara.view.main.HeaderView;
import com.pucara.view.main.MainView;
import com.pucara.view.purchase.PurchaseView;
import com.pucara.view.report.ReportView;
import com.pucara.view.sale.SaleView;
import com.pucara.view.stock.StockView;

/**
 * This class represents the Header controller in the system.
 * 
 * @author Maximiliano
 */
public class HeaderController {
	private HeaderView headerView;
	private MainView mainView;

	public HeaderController(HeaderView headerView, MainView mainView) {
		this.headerView = headerView;
		this.mainView = mainView;
	}

	/**
	 * Not supported so far.
	 * 
	 * @return MouseListener
	 */
	public MouseListener createCategoryListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainView.removeCentralPanel();
				mainView.addNewCentralPanel(new CategoryView());
				// headerView.changeToBold(headerView.getCategoryLabel());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// headerView.changeCursorLabel(headerView.getCategoryLabel());
				// headerView.changeToBold(headerView.getCategoryLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// headerView.changeToLight(headerView.getCategoryLabel());
			}
		};
	}

	/**
	 * 
	 * @return MouseListener
	 */
	public MouseListener createStockListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainView.removeCentralPanel();
				StockView stockView = new StockView();
				mainView.addNewCentralPanel(stockView);
				headerView.changeToBold(headerView.getStockLabel());
				stockView.setFocusOnInput();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getStockLabel());
				// headerView.changeToBold(headerView.getStockLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// headerView.changeToLight(headerView.getStockLabel());
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
				mainView.removeCentralPanel();
				SaleView saleView = new SaleView();
				mainView.addNewCentralPanel(saleView);
				headerView.changeToBold(headerView.getSaleLabel());
				saleView.setFocusOnInput();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getSaleLabel());
				// headerView.changCeToBold(headerView.getSaleLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// headerView.changeToLight(headerView.getSaleLabel());
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
				headerView.changeToBold(headerView.getCloseLabel());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getCloseLabel());
				// headerView.changeToBold(headerView.getCloseLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// headerView.changeToLight(headerView.getCloseLabel());
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
				mainView.removeCentralPanel();
				mainView.addNewCentralPanel(new ReportView());
				headerView.changeToBold(headerView.getReportsLabel());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getReportsLabel());
				// headerView.changeToBold(headerView.getReportsLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// headerView.changeToLight(headerView.getReportsLabel());
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
				mainView.removeCentralPanel();
				PurchaseView purchaseView = new PurchaseView();
				mainView.addNewCentralPanel(purchaseView);
				headerView.changeToBold(headerView.getPurchaseLabel());
				purchaseView.setFocusOnInput();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				headerView.changeCursorLabel(headerView.getPurchaseLabel());
				// headerView.changeToBold(headerView.getPurchaseLabel());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// headerView.changeToLight(headerView.getPurchaseLabel());
			}
		};
	}

}

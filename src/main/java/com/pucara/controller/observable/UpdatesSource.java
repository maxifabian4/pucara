package com.pucara.controller.observable;

import java.util.Observable;

public class UpdatesSource extends Observable {
	public UpdatesSource() {

	}

	public void catchUpdate(String barcode) {
		setChanged();
		notifyObservers(barcode);
	}
}

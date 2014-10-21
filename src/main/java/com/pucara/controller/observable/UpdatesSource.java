package com.pucara.controller.observable;

import java.util.List;
import java.util.Observable;

import com.pucara.core.entities.PartialElement;

public class UpdatesSource extends Observable {
	public UpdatesSource() {

	}

	public void catchUpdate(String barcode) {
		setChanged();
		notifyObservers(barcode);
	}

	public void catchUpdate(List<PartialElement> partialList) {
		for (PartialElement partialElement : partialList) {
			setChanged();
			notifyObservers(partialElement.getBarcode());
		}
	}
}

package com.pucara.core.entities;

import java.util.ArrayList;
import java.util.List;

import com.pucara.common.CommonMessageError;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.Response;

/**
 * This class represents a Collection of products.
 * 
 * @author Maximiliano
 */
public class ProductsCollection {

	private List<PartialElement> products;

	public ProductsCollection() {
		products = new ArrayList<PartialElement>();
	}

	/**
	 * Add a new product to the list.
	 * 
	 * @param barcode
	 */
	public void addProduct(Product product) {
		PartialElement storedProduct = alreadyExists(product.getBarcode());

		if (storedProduct != null) {
			storedProduct.increaseProduct();
		} else {
			products.add(new PartialElement(product, 1));
		}
	}

	/**
	 * Returns all products in the list.
	 * 
	 * @return List
	 */
	public List<PartialElement> getAllProducts() {
		return products;
	}

	/**
	 * Removes a product from the collection.
	 * 
	 * @param barcode
	 * @return boolean
	 */
	public boolean removeProduct(String barcode) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getBarcode().equals(barcode)) {
				products.remove(i);
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the collection's size.
	 * 
	 * @return int
	 */
	public int getSize() {
		return products.size();
	}

	/**
	 * Returns a specific product at "position".
	 * 
	 * @param position
	 * @return Product
	 */
	public Product getProductAt(int position) {
		return products.get(position);
	}

	/**
	 * Returns the quantity of a product at "position".
	 * 
	 * @param position
	 * @return Integer
	 */
	public Integer getQuantityOfProductAt(int position) {
		return products.get(position).getQuantity();
	}

	/**
	 * Verifies if a product already exists in the list.
	 * 
	 * @param barcode
	 * @return PartialElement
	 */
	public PartialElement alreadyExists(String barcode) {

		for (PartialElement storedProduct : products) {
			if (storedProduct.getBarcode().equals(barcode)) {
				return storedProduct;
			}
		}

		return null;
	}

	/**
	 * Decreases the quantity Product value and it will be removed if quantity
	 * is 0 (zero).
	 * 
	 * @param barcode
	 * @return Response
	 */
	public Response decreaseQuantityProduct(String barcode) {
		PartialElement existingProduct = alreadyExists(barcode);

		if (existingProduct != null) {
			existingProduct.decreaseProduct();

			if (existingProduct.getQuantity().equals(0)) {
				removeProduct(barcode);
			}

			return new Response();
		} else {
			return new Response(new ErrorMessage(ErrorType.ELEMENT_NOT_FOUND,
					CommonMessageError.ELEMENT_NOT_FOUND + " - " + barcode));
		}
	}

	public Product getProductBy(String barcode) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getBarcode().equals(barcode)) {
				return products.get(i);
			}
		}

		return null;
	}

	@Override
	public String toString() {
		String productsResult = "";

		for (PartialElement element : products) {
			productsResult += String.format("[%s, %d]", element.getBarcode(),
					element.getQuantity());
		}

		return productsResult;
	}

	public Response increaseQuantityProduct(String barcode, int n) {
		PartialElement existingProduct = alreadyExists(barcode);

		if (existingProduct != null) {
			existingProduct.increaseProduct(n);
			return new Response();
		} else {
			return new Response(new ErrorMessage(ErrorType.ELEMENT_NOT_FOUND,
					CommonMessageError.ELEMENT_NOT_FOUND + " - " + barcode));
		}
	}

}

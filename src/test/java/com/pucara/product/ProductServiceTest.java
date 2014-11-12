package com.pucara.product;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.Product;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.NewProductRequest;
import com.pucara.core.response.ProductResponse;
import com.pucara.core.services.product.ProductService;

import junit.framework.Assert;

/**
 * Unit test for simple App.
 */
public class ProductServiceTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductServiceTest.class);

	private void addProduct() {
		String barcode = Utilities.generateRandomNumber(7);
		String description = Utilities.generateRandomData(5);

		// Populate new product information.
		Product newProduct = new Product();
		newProduct.setBarcode(barcode);
		newProduct.setDescription(description);
		newProduct.setInitialCost(7.5);
		newProduct.setFinalCost(9.75);
		newProduct.setPercentage(30.0);
		newProduct.setStock(30);
		newProduct.setMinStock(3);
		newProduct.setByPercentage(true);
		newProduct.setCategoryId(100);
		newProduct.setDate(Utilities.getCurrentDate());

		// Set new product to the request.
		NewProductRequest newProductRequest = new NewProductRequest(newProduct);

		// Add new product to the database.
		ProductResponse response = ProductService.addProduct(newProductRequest);

		Assert.assertTrue("Product could not be added",
				response.wasSuccessful());
		LOGGER.debug("Product {} added succesfully.", newProduct);
	}

	@Test
	public void addNProducts() {
		// Establish connection.
		MySqlAccess.establishConection();

		int iterations = 1;

		for (int i = 0; i < iterations; i++) {
			addProduct();
		}
	}

}

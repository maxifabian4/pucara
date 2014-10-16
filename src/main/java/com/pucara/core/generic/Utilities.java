package com.pucara.core.generic;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.pucara.common.CommonData;
import com.pucara.core.entities.Category;
import com.pucara.core.entities.Product;
import com.pucara.core.entities.report.PurchaseDailyReport;

public class Utilities {

	/**
	 * Generate random data with a specified size.
	 * 
	 * @return String
	 */
	public static String generateRandomData(int size) {
		Random rng = new Random();

		String alphanumeric = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789";

		String final_string = "";

		for (int x = 0; x < size; x++) {
			final_string = final_string.concat(Character.toString(alphanumeric
					.charAt(rng.nextInt(alphanumeric.length()))));
		}

		return final_string;
	}

	/**
	 * Generate random number with a specified size.
	 * 
	 * @return String
	 */
	public static String generateRandomNumber(int size) {
		Random rng = new Random();

		String alphanumeric = "0123456789";

		String final_string = "";

		for (int x = 0; x < size; x++) {
			final_string = final_string.concat(Character.toString(alphanumeric
					.charAt(rng.nextInt(alphanumeric.length()))));
		}

		return final_string;
	}

	/**
	 * Returns the current date based on the system configuration.
	 * 
	 * @return String
	 */
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	/**
	 * Calculates the current sale price for a product. Also, determines how to
	 * round the final result.
	 * 
	 * @param product
	 * @return double
	 */
	// public static double getTotalProductPrice(Product product) {
	// double cost = product.getCost();
	// // int percentage = product.getPercentage();
	// // double division = (double) percentage / 100;
	//
	// return cost;
	// }

	/**
	 * 
	 * @param count
	 * @param productAt
	 * @return
	 */
	public static double getProductGain(Product product, Integer count) {
		double cost = product.getInitialCost();
		double percentage = product.getPercentage();
		double division = (double) percentage / 100;

		return cost * division * count;
	}

	/**
	 * 
	 * @param product
	 * @return
	 */
	// public static Double getProductSalePrice(Product product) {
	// double cost = product.getCost();
	// int percentage = product.getPercentage();
	// double division = (double) percentage / 100;
	// Double value = cost * division + cost;
	//
	// return truncateDecimal(value, 2).doubleValue();
	// }

	/**
	 * @param x
	 * @param numberofDecimals
	 * @return
	 */
	public static Double adjustDecimals(double x) {
		double[] potentialDecimals = { 0, 0.25, 0.5, 0.75, 1 };
		// Truncate in two decimals
		int i = (int) x;
		x = x - i;

		BigDecimal truncated = new BigDecimal(String.valueOf(x)).setScale(2,
				BigDecimal.ROUND_UP);

		int cont = 0;
		while (cont < potentialDecimals.length
				&& truncated.doubleValue() > potentialDecimals[cont]) {
			cont++;
		}

		return i + potentialDecimals[cont];
	}

	public static double truncateDecimal(double x, int numberofDecimals) {
		if (x > 0) {
			return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals,
					BigDecimal.ROUND_FLOOR).doubleValue();
		} else {
			return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals,
					BigDecimal.ROUND_CEILING).doubleValue();
		}
	}

	/**
	 * Generates a list of rows, in order to add each category in a table.
	 * 
	 * @param allCategories
	 * @return List<String[]>
	 */
	public static List<String[]> generateArrayRowsCategory(
			List<Category> allCategories) {
		List<String[]> rows = new ArrayList<String[]>();

		for (Category category : allCategories) {
			rows.add(new String[] { category.getName().toString(),
					category.getDescription().toString() });
		}

		return rows;
	}

	/**
	 * Generates a list of rows, in order to add each product in a table.
	 * 
	 * @param allProducts
	 * @return List<String[]>
	 */
	// public static List<String[]> generateArrayRowsProduct(
	// List<Product> allProducts) {
	// List<String[]> rows = new ArrayList<String[]>();
	//
	// for (Product product : allProducts) {
	// rows.add(new String[] { product.getBarcode().toString(),
	// product.getDescription().toString(),
	// product.getStock().toString(),
	// truncateDecimal(product.getCost(), 2).toString() });
	// }
	//
	// return rows;
	// }

	/**
	 * 
	 * @param allPurchases
	 * @return
	 */
	public static List<String[]> generateArrayRowsPurchase(
			List<PurchaseDailyReport> allPurchases) {
		List<String[]> rows = new ArrayList<String[]>();

		for (PurchaseDailyReport element : allPurchases) {
			rows.add(new String[] { element.getPurchaseDescription(),
					String.valueOf(element.getExpense()) });
		}

		return rows;
	}

	/**
	 * Generates a list of categories, in order to populate in a comboBox.
	 * 
	 * @param allCategories
	 * @return Category[]
	 */
	public static Category[] generateArrayCategories(
			List<Category> allCategories) {
		Category[] array = new Category[allCategories.size()];

		for (int i = 0; i < allCategories.size(); i++) {
			array[i] = allCategories.get(i);
		}

		return array;
	}

	/**
	 * Determines if a String contains digits.
	 * 
	 * @param barcode
	 * @return boolean
	 */
	public static boolean areAllDigits(String barcode) {
		String regex = "[0-9]+";

		return barcode.matches(regex);
	}

	/**
	 * 
	 * @param stringValue
	 * @param longValue
	 * @return boolean
	 */
	public static boolean isTooLong(String stringValue, Integer longValue) {
		return stringValue.length() > longValue;
	}

	/**
	 * 
	 * @param value
	 * @return int
	 */
	public static int getStringWidth(String value) {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true,
				true);
		Font font = new Font(CommonData.GENERAL_FONT, Font.PLAIN,
				CommonData.GENERAL_FONT_SIZE_TABLE);

		return (int) (font.getStringBounds(value, frc).getWidth());
	}

	/**
	 * 
	 * @param command
	 * @return String
	 */
	public String executeCommand(String[] command) {
		StringBuffer output = new StringBuffer();
		Process p;

		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}

	/**
	 * remove!
	 * 
	 * @param gain
	 * @return
	 */
	// public static String getDoubleFormat(double value) {
	// value = Math.floor(value * 100) / 100;
	//
	// return String.valueOf(value);
	// }

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static Double getDoubleValue(String stringValue) {
		try {
			Double resultValue = Double.valueOf(stringValue.trim());

			return resultValue;
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static Integer getIntegerValue(String stringValue) {
		try {
			Integer resultValue = Integer.valueOf(stringValue.trim());

			return resultValue;
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	/**
	 * REMOVE !!!
	 * 
	 * @param allProducts
	 * @return
	 */
	// public static ListStockProduct[] generateListObjectProducts(List<Product>
	// allProducts) {
	// ListStockProduct[] list = new ListStockProduct[allProducts.size()];
	// String barcode, description, stock, price;
	// Integer minStock;
	// Product product;
	//
	// for (int i = 0; i < allProducts.size(); i++) {
	// product = allProducts.get(i);
	// barcode = product.getBarcode();
	// description = product.getDescription();
	// stock = product.getStock().toString();
	// price = truncateDecimal(product.getCost(), 2).toString();
	// minStock = product.getMinStock();
	//
	// // if (product.getStock() <= product.getMinStock()) {
	// // minStock = true;
	// // }
	//
	// list[i] = new ListStockProduct(barcode, description, stock, minStock,
	// price);
	// // minStock = false;
	// }
	//
	// return list;
	// }

	public static String getSpanishDay(String dayName) {
		dayName = dayName.substring(0, 3);

		switch (dayName) {
		case "mon":
			return "lun";
		case "tue":
			return "mar";
		case "wed":
			return "mie";
		case "thu":
			return "jue";
		case "fri":
			return "vie";
		case "sat":
			return "sab";
		case "sun":
			return "dom";
		}

		return null;
	}

	public static String getSpanishMonth(String month) {
		month = month.substring(0, 3);

		switch (month) {
		case "jan":
			return "ene";
		case "feb":
			return "feb";
		case "mar":
			return "mar";
		case "apr":
			return "abr";
		case "may":
			return "may";
		case "jun":
			return "jun";
		case "jul":
			return "jul";
		case "aug":
			return "ago";
		case "sep":
			return "sep";
		case "oct":
			return "oct";
		case "nov":
			return "nov";
		case "dec":
			return "dic";
		}

		return null;
	}

}

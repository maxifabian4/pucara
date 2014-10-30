package com.pucara.common;

import java.awt.Color;

/**
 * Contains the common data used in the application.
 * 
 * @author Maximiliano
 */
public class CommonData {

	/**
	 * System colors.
	 */
	public static final Color DARK_FONT_COLOR = Color.DARK_GRAY;
	public static final Color LIGHT_FONT_COLOR = new Color(237, 237, 237);
	public static final Color GENERAL_BACKGROUND_COLOR = new Color(250, 250,
			250);
	// public static final Color GENERAL_BACKGROUND_COLOR = new Color(228, 228,
	// 228);
	public static final Color BACKGROUND_TABLE_COLOR = new Color(241, 241, 241);
	public static final Color DEFAULT_BUTTON_COLOR = new Color(255, 52, 72);
	public static final Color BACKGROUND_PANEL_COLOR = new Color(8, 167, 215);
	public static final Color MOUSEOVER_SCROLL_COLOR = DARK_FONT_COLOR;
	public static final Color DEFAULT_SELECTION_COLOR = new Color(109, 158, 235);
	public static final Color SALE_CHART_COLOR = new Color(147, 196, 125);
	public static final Color EXPENSE_CHART_COLOR = new Color(221, 126, 107);

	/**
	 * General data for table.
	 */
	public static final String[] CATEGORY_COLUMNS = new String[] {
			"categor\u00EDa", "descripci\u00F3n" };
	public static final String[] PRODUCT_COLUMNS = new String[] { "barcode",
			"descripci\u00F3n", "stock", "costo" };
	public static final String[] PARTIAL_PRODUCT_COLUMNS = new String[] {
			"barcode", "descripci\u00F3n", "subcantidad", "subtotal" };

	public static final String[] PURCHASE_COLUMNS = new String[] {
			"descripci\u00F3n", "costo" };
	public static final boolean[] PURCHASE_COLUMNS_EDITABLE = new boolean[] {
			false, false };
	public static final Integer[] PURCHASE_COLUMNS_SCALABLE = new Integer[] {
			null, null };

	public static final boolean[] CATEGORY_COLUMNS_EDITABLE = new boolean[] {
			false, false };
	public static final Integer[] CATEGORY_COLUMNS_SCALABLE = new Integer[] {
			null, null, null };

	public static final boolean[] PRODUCT_COLUMNS_EDITABLE = new boolean[] {
			false, false, false, false };
	public static final Integer[] PRODUCT_COLUMNS_SCALABLE = new Integer[] {
			null, 400, null, null };

	public static final boolean[] PARTIAL_PRODUCT_COLUMNS_EDITABLE = new boolean[] {
			false, false, false, false };
	public static final Integer[] PARTIAL_PRODUCT_COLUMNS_SCALABLE = new Integer[] {
			100, 400, null, null };
	public static final int FIRST_ROW = 0;

	public static final int BARCODE_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	public static final int COST_COLUMN = 2;
	public static final int PERCENTAGE_COLUMN = 3;
	public static final int STOCK_COLUMN = 4;
	public static final int MIN_STOCK_COLUMN = 5;
	public static final int NAME_CATEGORY_COLUMN = 0;

	/**
	 * Constraints.
	 */
	public static final Integer MAX_LONG_VALUE_PRODUCT_DESCRIPTION = 50;
	public static final Long DEFAULT_LONG_IDENTIFIER = 0L;

	/**
	 * Conditions for queries.
	 */
	public static final String PRODUCT_WHERE_BARCODE = "WHERE barcode = '%s'";
	public static final String CATEGORY_WHERE_ID = "WHERE id = %s";

	/**
	 * Statements.
	 */
	public static final String GAIN_BY_DAY = "SELECT LOWER(dayname(from_days(to_days(s.date)))) AS 'day', from_days(to_days(s.date)) AS 'date', "
			+ "SUM(gain) AS 'gain', SUM( p.finalcost * xssdp.count ) AS  'value'"
			+ "FROM sale s JOIN x_sale_sale_detail_product xssdp ON ( s.id = xssdp.sale_id )"
			+ "JOIN product p ON ( p.barcode = xssdp.barcode )"
			+ "WHERE s.date > date_add(curdate(), INTERVAL %d DAY) "
			+ "GROUP BY from_days(to_days(s.date)) " + "ORDER BY s.date ASC";

	public static final String EXPENSE_BY_DAY = "SELECT LOWER(dayname(from_days(to_days(date)))) AS 'day', from_days(to_days(date)) AS 'date', "
			+ "SUM(expense) AS 'value' "
			+ "FROM purchase "
			+ "WHERE date > date_add(curdate(), INTERVAL %d DAY) "
			+ "GROUP BY from_days(to_days(date)) " + "ORDER BY date ASC";

	public static final String GAIN_BY_YEAR = "SELECT LOWER(monthname(from_days(to_days(date)))) AS 'month', SUM(gain) AS 'value' "
			+ "FROM sale "
			+ "WHERE YEAR(date) = %d "
			+ "GROUP BY monthname(from_days(to_days(date))) "
			+ "ORDER BY date ASC";

	public static final String EXPENSE_BY_YEAR = "SELECT LOWER(monthname(from_days(to_days(date)))) AS 'month', SUM(expense) AS 'value' "
			+ "FROM purchase "
			+ "WHERE YEAR(date) = %d AND id NOT IN (SELECT purchase_id FROM x_purchase_purchase_detail)"
			+ "GROUP BY monthname(from_days(to_days(date))) "
			+ "ORDER BY date ASC";

	/**
	 * Fonts.
	 */
	// Remove Ubuntu font ...
	public static String GENERAL_FONT = "Ubuntu366";
	public static String ROBOTO_LIGHT_FONT = "Roboto";
	public static final int GENERAL_FONT_SIZE_TABLE = 18;
	public static final int FONT_SIZE_NUM_REPORT = 20;
	public static final int MAX_HEIGHT_TABLE_PANEL = 650;
	public static final int GENERAL_FONT_SIZE = 15;
	public static final int GENERAL_FONT_SIZE_TITLE_FORM = 35;
	public static final int GENERAL_FONT_SIZE_LABEL = 21;

	/**
	 * General messages for the UI components.
	 */
	public static final String SEARCH_FIELD_MESSAGE = "ingrese descripci\u00F3n o c\u00F3digo del elemento ..."; // remove!
	public static final String EMPTY_STRING = "";

	/**
	 * Database information constants.
	 */
	// public static final String DATABASE_NAME = "pucaratest";
	public static final Object SALE_TABLE = "id";
	public static final String X_SALE_SALE_DETAIL_TABLE = "x_sale_sale_detail";
	public static final Object X_SALE_SALE_DETAIL_PRODUCT_TABLE = "x_sale_sale_detail_product";
	public static final String SALE_ID_COLUMN = "sale_id";
	public static final String SALE_DETAIL_ID_COLUMN = "sale_detail_id";

	/**
	 * Resources path.
	 */
	public static final String IMAGES_PATH = "/images/";
	public static final String DB_PROPERTIES_PATH = "src/main/resources/properties/db.properties";
	// public static final String REPORT_PROPERTIES_PATH =
	// "src/main/resources/properties/report.properties";
	// public static final String DB_PROPERTIES_PATH =
	// "/home/pucara/Programas/pucara/properties/db.properties";
	
//	public static final String REPORT_PROPERTIES_PATH = "/home/pucara/Programas/pucara/properties/report.properties";
	public static final String REPORT_PROPERTIES_PATH = "src/main/resources/properties/report.properties";
}
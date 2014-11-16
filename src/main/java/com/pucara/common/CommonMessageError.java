package com.pucara.common;

/**
 * Common message errors used in the system.
 * 
 * @author Maximiliano Fabian
 */
public class CommonMessageError {
	public static final String INVALID_BARCODE_FORMAT = "C\u00F3digo de barras inv\u00E1lido";
	public static final String DUPLICATED_PRODUCT = "El producto ('%s', '%s') ya existe en el sistema.";
	public static final String DUPLICATED_CATEGORY = "La categor\u00EDa ('%s') ya existe en el sistema.";
	public static final String INVALID_DESCRIPTION_FORMAT = "La descripci\u00F3n del producto es muy larga (max: %d caracteres).";
	public static final String DESCRIPTION_EMPTY_VALUE = "La descripci\u00F3n del producto no debe ser vac\u00EDa.";
	public static final String INVALID_DOUBLE_FORMAT = "(%s) es un formato inv\u00E1lido.";
	public static final String INVALID_INTEGER_FORMAT = "(%s) es un formato inv\u00E1lido.";
	public static final String ELEMENT_NOT_FOUND = "El elemento no se ha encontrado.";
	public static final String WARNING_REMOVE_CATEGORY = "Est\u00E1 seguro que desea eliminar el elemento '%s'";
	public static final String BARCODE_NOT_FOUND = "El c\u00F3digo de barras [%s] no se ha encontrado.";
	public static final String EMPTY_PARTIAL_LIST = "No se encuentran productos seleccionados para efectuar una venta.";
	public static final String INSUFFICIENT_STOCK = "El producto [%s] no posee suficiente stock.";
	public static final String UPDATE_PRODUCT_ERROR = "El producto [%s] no se ha modificado.";
	public static final String UPDATE_PRODUCTS_ERROR = "Error al modificar el stock de un prodructo.";
	public static final String BARCODE_DESCRIPTION_NO_DETECTED = "No se ha detectado un c\u00F3digo de barras o descripci\u00F3n del producto.";
	public static final String BARCODE_DESCRIPCTION_INVALID = "C\u00F3digo de barras o descripci\u00F3n invalido, ingrese nuevamente la informaci\u00F3n.";
	public static final String INSERTION_ERROR = "Error tratando de ingresar informaci\u00F3n al sistema.";
	public static final String STATEMENT_SALE_ERROR = "Error tratanto de obtener informaci\u00F3n de la vista de ventas.";
	public static final String STATEMENT_PURCHASE_ERROR = "Error tratanto de obtener informaci\u00F3n de la vista de costos.";
	public static final String STATEMENT_CHART_ERROR = "Error tratanto de obtener informaci\u00F3n correspondiente a un gr\u00E1fico.";
	public static final String EXPORT_ERROR = "Error tratando de exportar informaci\u00F3n de la entidad '%s', desde la base de datos.";
}

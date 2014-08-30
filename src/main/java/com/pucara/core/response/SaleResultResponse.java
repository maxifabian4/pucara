package com.pucara.core.response;

/**
 * 
 * @author Maximiliano
 */
public class SaleResultResponse extends Response {
	private Long saleId;
	private Long saleDetailId;
	private String allProducts;

	public SaleResultResponse(Long saleId, Long saleDetailId, String allProducts) {
		super();
		this.saleId = saleId;
		this.saleDetailId = saleDetailId;
		this.allProducts = allProducts;
	}

	public SaleResultResponse(ErrorMessage me) {
		super(me);
		this.saleId = null;
		this.saleDetailId = null;
		this.allProducts = null;
	}

	public Long getSaleId() {
		return saleId;
	}

	public Long getSaleDetailId() {
		return saleDetailId;
	}

	public String getAllProductsString() {
		return allProducts;
	}

}

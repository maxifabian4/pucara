package com.pucara.persistence.mapper;

import java.util.List;

public interface ProductMapper {

	public List<String> getAllDescriptions();

	public String getBarcodeByDescription(String description);

}

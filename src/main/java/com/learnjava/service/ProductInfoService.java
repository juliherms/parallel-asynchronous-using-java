package com.learnjava.service;

import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.ProductOption;

import java.util.List;

import static com.learnjava.util.CommonUtil.delay;

/**
 * This class responsible to simulate Product microservice about info
 */
public class ProductInfoService {

    /**
     * Return product info by product ID
     * @param productId
     * @return
     */
    public ProductInfo retrieveProductInfo(String productId) {

        delay(1000); //simulate retrieve
        List<ProductOption> productOptions = List.of(new ProductOption(1, "64GB", "Black", 699.99),
                new ProductOption(2, "128GB", "Black", 749.99));
        return ProductInfo.builder().productId(productId)
                .productOptions(productOptions)
                .build();
    }
}

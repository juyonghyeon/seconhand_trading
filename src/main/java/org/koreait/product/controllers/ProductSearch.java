package org.koreait.product.controllers;

import lombok.Data;
import org.koreait.global.search.CommonSearch;
import org.koreait.product.constants.ProductStatus;

import java.util.List;

@Data
public class ProductSearch extends CommonSearch {
    private List<ProductStatus> productStatus;
}

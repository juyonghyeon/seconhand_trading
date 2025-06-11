package org.koreait.product.controllers;

import lombok.Data;
import org.koreait.global.search.CommonSearch;
import org.koreait.member.constants.Authority;

import java.util.List;

@Data
public class ProductSearch extends CommonSearch {
    private List<Authority> authority;
}

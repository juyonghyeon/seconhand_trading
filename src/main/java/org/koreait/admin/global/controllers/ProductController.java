package org.koreait.admin.global.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.search.ListData;
import org.koreait.product.controllers.ProductSearch;
import org.koreait.product.entities.Product;
import org.koreait.product.services.ProductInfoService;
import org.koreait.product.services.ProductManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController extends CommonController {

    private final ProductInfoService infoService;
    private final ProductManageService manageService;


    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "product";
    }

    @GetMapping({"","/list"})
    public String list(@ModelAttribute("productSearch") ProductSearch search, Model model) {
        commonProcess("list", model);

        ListData<Product> data = infoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "admin/product/list";
    }

    @RequestMapping({"","/list"})
    public String listPs(@RequestParam(name="chk", required = false)List<Integer> chks, Model model) {
        manageService.processBatch(chks);

        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }


    private void commonProcess(String code, Model model) {
        code = StringUtils.hasText(code) ? code : "list";
        String pageTitle = "";

        if (code.equals("list")) { // 회원 목록
            pageTitle = "상품목록";
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subCode", code);
    }

}

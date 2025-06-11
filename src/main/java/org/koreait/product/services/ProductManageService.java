package org.koreait.product.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.exceptions.script.AlertException;
import org.koreait.global.libs.Utils;
import org.koreait.product.constants.ProductStatus;
import org.koreait.product.entities.Product;
import org.koreait.product.repositories.ProductRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Lazy
@Service
@RequiredArgsConstructor
public class ProductManageService {

    private final Utils utils;
    private final HttpServletRequest request;
    private final ProductRepository repository;

    public void processBatch(List<Integer> chks) {

        if (chks == null || chks.isEmpty()) {
            throw new AlertException("처리할 상품을 선택하세요.", HttpStatus.BAD_REQUEST);
        }


        String method = request.getMethod();
        List<Product> products = new ArrayList<>();
        for (int chk : chks) {
            Long seq = Long.valueOf(utils.getParam("seq_" + chk));
            Product product = repository.findById(seq).orElse(null);
            if (product == null) continue;
            if (method.equalsIgnoreCase("DELETE")) {
                product.setDeletedAt(LocalDateTime.now());
            } else {
                boolean updateStatus = Boolean.parseBoolean(Objects.requireNonNullElse(utils.getParam("updateStatus_" + chk), "false"));
                if (updateStatus) {
                    Optional.ofNullable(utils.getParam("status_" + chk))
                            .map(ProductStatus::valueOf)
                            .ifPresent(product::setStatus);
                    /*
                    Optional.ofNullable(utils.getParam("status_" + chk))
                            .flatMap(str -> {
                                try {
                                    return Optional.of(ProductStatus.valueOf(str));
                                } catch (IllegalArgumentException e) {
                                    return Optional.empty(); // 무시하거나 로그 출력 가능
                                }
                            })
                            .ifPresent(product::setStatus);

                     */

                }
            }
            products.add(product);
        }
        repository.saveAll(products);
    }
}

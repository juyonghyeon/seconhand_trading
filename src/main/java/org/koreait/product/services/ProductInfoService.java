package org.koreait.product.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.search.ListData;
import org.koreait.global.search.Pagination;
import org.koreait.product.constants.ProductStatus;
import org.koreait.product.controllers.ProductSearch;
import org.koreait.product.entities.Product;
import org.koreait.product.repositories.ProductRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class ProductInfoService {

    private final ProductRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private final HttpServletRequest request;

    // 상품 목록
    public ListData<Product> getList(ProductSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;

        List<String> addWhere = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {
            if (sopt.equalsIgnoreCase("NAME")) {
                addWhere.add("name LIKE ?");
            } else if (sopt.equalsIgnoreCase("CATEGORY")) {
                addWhere.add("category LIKE ?");
            } else {
                addWhere.add("CONCAT(name, category LIKE ?");
            }

            params.add("%" + skey + "%");
        }

        StringBuffer sb = new StringBuffer(2000);
        StringBuffer sb2 = new StringBuffer(2000);
        sb.append("SELECT * FROM PRODUCT");
        sb2.append("SELECT COUNT(*) FROM PRODUCT");

        if (!addWhere.isEmpty()) {
            String where = " WHERE " + String.join(" AND ", addWhere);
            sb.append(where);
            sb2.append(where);
        }

        sb.append(" ORDER BY createdAt DESC");
        sb.append(" LIMIT ?, ?");

        int total = jdbcTemplate.queryForObject(sb2.toString(), int.class, params.toArray());

        params.add(offset);
        params.add(limit);

        List<Product> items = jdbcTemplate.query(sb.toString(), this::mapper, params.toArray());

        Pagination pagination = new Pagination(page, total, 10, 10, request);

        return new ListData<>(items, pagination);
    }



    private Product mapper(ResultSet rs, int i) throws SQLException {
        Product item = new Product();

        item.setSeq(rs.getLong("seq"));
        item.setGid(rs.getString("gid"));
        item.setName(rs.getString("name"));
        item.setCategory(rs.getString("category"));
        item.setStatus(ProductStatus.valueOf(rs.getString("status"))); // enum 처리
        item.setConsumerPrice(rs.getInt("consumerPrice"));
        item.setSalePrice(rs.getInt("salePrice"));
        item.setDescription(rs.getString("description"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        Timestamp modifiedAt = rs.getTimestamp("modifiedAt");
        Timestamp deletedAt = rs.getTimestamp("deletedAt");

        item.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        item.setModifiedAt(modifiedAt == null ? null : modifiedAt.toLocalDateTime());
        item.setDeletedAt(deletedAt == null ? null : deletedAt.toLocalDateTime());

        return item;
    }

}

package org.koreait.product.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.product.controllers.RequestProduct;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.search.ListData;
import org.koreait.global.search.Pagination;
import org.koreait.product.constants.ProductStatus;
import org.koreait.product.controllers.ProductSearch;
import org.koreait.product.entities.Product;
import org.koreait.product.exceptions.ProductNotFoundException;
import org.koreait.product.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Lazy
@Service
@RequiredArgsConstructor
public class ProductInfoService {

    private final ModelMapper modelMapper;
    private final ProductRepository repository;
    private final FileInfoService fileInfoService;
    private final JdbcTemplate jdbcTemplate;
    private final HttpServletRequest request;

    public Product get(Long seq) {

        Product item = repository.findById(seq).orElseThrow(ProductNotFoundException::new);
        addInfo(item); // 추가 정보 처리

        return item;
    }

    public RequestProduct getForm(Long seq) {
        Product item = get(seq);
        return modelMapper.map(item, RequestProduct.class);
    }

    public ListData<Product> getList(ProductSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;

        String sopt = search.getSopt();
        String skey = search.getSkey();

        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        String sql = "SELECT * FROM PRODUCT";
        String countSql = "SELECT COUNT(*) FROM PRODUCT";

        List<Object> params = new ArrayList<>();

        StringBuffer sb1 = new StringBuffer(5000);
        StringBuffer sb2 = new StringBuffer(5000);
        sb1.append(sql);
        sb2.append(countSql);

        List<String> arrWhere = new ArrayList<>();

        /* 상품 등록일자 검색 처리 S */
        if (sDate != null) {
            arrWhere.add("createdAt >= ?");
            params.add(sDate.atStartOfDay());
        }

        if (eDate != null) {
            arrWhere.add("createdAt <= ?");
            params.add(eDate.atTime(23, 59, 59));
        }

        /* 상품 등록일자 검색 처리 E */

        /* 키워드 검색 처리 S */
        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            // 상품명으로 검색
            String conds = null;
            if (sopt.equals("NAME")) {
                conds = "name LIKE ?";
            } else if (sopt.equals("DESCRIPTION")) { // 상세 설명
                conds = "description LIKE ?";
            } else { // 통합검색
                conds = "CONCAT(name, description) LIKE ?";
            }
            arrWhere.add(conds);
            params.add("%" + skey + "%");
        }
        /* 키워드 검색 처리 E */

        // 판매가, 소비자가 검색 처리 S
        Integer sPrice = search.getSPrice();
        Integer ePrice = search.getEPrice();
        if (sPrice != null) {
            arrWhere.add("(salePrice >= ? OR consumerPrice >= ?)");
            params.add(sPrice);
        }

        if (ePrice != null) {
            arrWhere.add("(salePrice <= ? OR consumerPrice <= ?)");
            params.add(ePrice);
        }
        // 판매가, 소비자가 검색 처리 E

        if (!arrWhere.isEmpty()) {
            String where = " WHERE " + arrWhere.stream().collect(Collectors.joining(" AND "));
            sb1.append(where);
            sb2.append(where);
        }

        /* 페이징 처리 S */



        // 전체 갯수
        int total = jdbcTemplate.queryForObject(sb2.toString(), int.class, params.toArray());

        sb1.append(" ORDER BY createdAt DESC LIMIT ?, ?");
        params.add(offset);
        params.add(limit);

        // 상품 목록 조회
        List<Product> items = jdbcTemplate.query(sb1.toString(), this::mapper, params.toArray());
        items.forEach(this::addInfo); // 추가 정보 처리

        Pagination pagination = new Pagination(page, total, 10, limit, request);
        /* 페이징 처리 E */

        return new ListData<>(items, pagination);
    }

    /**
     * 상품 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(Product item) {
        /* 업로드한 파일 처리 S */
        String gid = item.getGid();
        item.setMainImages(fileInfoService.getList(gid, "main"));
        item.setListImages(fileInfoService.getList(gid, "list"));
        item.setEditorImages(fileInfoService.getList(gid, "editor"));
        /* 업로드한 파일 처리 E */
    }

    private Product mapper(ResultSet rs, int i) throws SQLException {
        Product item = new Product();
        item.setSeq(rs.getLong("seq"));
        item.setGid(rs.getString("gid"));
        item.setName(rs.getString("name"));
        item.setCategory(rs.getString("category"));
        item.setStatus(ProductStatus.valueOf(rs.getString("status")));
        item.setConsumerPrice(rs.getInt("consumerPrice"));
        item.setSalePrice(rs.getInt("salePrice"));
        item.setDescription(rs.getString("description"));
        item.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

        Timestamp modifiedAt = rs.getTimestamp("modifiedAt");
        Timestamp deletedAt = rs.getTimestamp("deletedAt");

        item.setModifiedAt(modifiedAt == null ? null : modifiedAt.toLocalDateTime());
        item.setDeletedAt(deletedAt == null ? null : deletedAt.toLocalDateTime());

        return item;
    }
}

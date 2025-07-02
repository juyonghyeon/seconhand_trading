package org.koreait.product.entities;

import lombok.Data;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.entities.BaseEntity;
import org.koreait.product.constants.ProductStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table("PRODUCT")
public class Product extends BaseEntity {
    @Id
    private Long seq;
    private String gid;
    private String name;
    private String category;
    private ProductStatus status;

    @Column("consumerPrice")
    private int consumerPrice;

    @Column("salePrice")
    private int salePrice;

    private String description;

    @Transient
    private List<FileInfo> listImages;

    @Transient
    private List<FileInfo> mainImages;

    @Transient
    private List<FileInfo> editorImages;
}
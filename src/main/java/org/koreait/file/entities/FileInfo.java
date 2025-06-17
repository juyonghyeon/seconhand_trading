package org.koreait.file.entities;

import lombok.Data;
import org.koreait.global.entities.BaseEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("FILE_INFO")
public class FileInfo extends BaseEntity {
    @Id
    private Long seq;
    private String gid;
    private String location;

    @Column("fileName")
    private String fileName;
    private String extension; // 확장자

    @Column("contentType")
    private String contentType; // 파일 종류

    @CreatedBy
    @Column("createdBy")
    private String createdBy; // 업로드한 로그인 사용자의 이메일

}

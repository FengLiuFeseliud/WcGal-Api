package com.wcacg.wcgal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;


@Data
@MappedSuperclass
public abstract class AbstractTimeEntity {
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    protected Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    protected Date updateTime;
}

package com.wcacg.wcgal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@MappedSuperclass
public abstract class AbstractTimeEntity {
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    protected Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    protected Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

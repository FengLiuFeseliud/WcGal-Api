package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id", unique = true, nullable = false)
    private long configId;

    @Column(name = "config_name", nullable = false)
    private String configName;

    @Column(name = "config_date", nullable = false)
    private String configDate;

    public Config(){

    }

    public Config(String configName, String configDate){
        this.configName = configName;
        this.configDate = configDate;
    }
}

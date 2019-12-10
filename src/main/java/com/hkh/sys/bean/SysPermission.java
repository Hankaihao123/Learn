package com.hkh.sys.bean;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {
    private Integer id;
    
    @JsonProperty(value = "parentId")
    private Integer pid;

    private String type;

    private String title;

    private String percode;

    private String icon;

    private String href;

    private String target;

    private Integer open;

    private Integer ordernum;

    private Integer available;

}
package com.hkh.sys.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysNotice {
    private Integer id;

    private String title;

    private Date createtime;

    private String opername;

    private String content;

}
package com.yk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/18 13:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private int id;
    private String userName;
    private Boolean sex;
}

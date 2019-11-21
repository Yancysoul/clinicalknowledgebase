package com.ywhk.ckb.dao.model.core;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author: hpy
 * @date: 2019-10-11 15:39
 * @description:
 */
@Data
@Entity
@Table(name = "core_user")
public class UserEntity {


    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "FUserId", nullable = false, length = 32)
    private String FUserId;

    @Column(name = "FUserName", nullable = false, length = 50)
    private String FUserName;

    @Column(name = "FPassWord", nullable = false, length = 50)
    private String FPassWord;

    @Column(name = "FNickName", nullable = false, length = 50)
    private String FNickName;

    @Column(name = "FSex", nullable = false)
    private int FSex;

    @Column(name = "FAge", nullable = false, length = 10)
    private String FAge;

    @Column(name = "FHeadImage", nullable = false, length = 225)
    private String FHeadImage;

    @Column(name = "FPhone", nullable = false, length = 20)
    private String FPhone;

    @Column(name = "FEmail", nullable = false, length = 50)
    private String FEmail;

    @Column(name = "FMark")
    private boolean FMark;
}

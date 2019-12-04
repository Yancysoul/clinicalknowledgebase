package com.ywhk.ckb.dao.model.core;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 人员
 */
@Data
@Entity
@Table(name = "SYS_User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Fuserid", nullable = false)
    private Integer FUserid;

    @Column(name = "Fgroupid", nullable = false)
    private Integer Fgroupid;

    @Column(name = "MechanismID", nullable = false)
    private Integer MechanismID;

    @Column(name = "FNumber", nullable = false, length = 50)
    private String FNumber;

    @Column(name = "FName", nullable = false, length = 50)
    private String FName;

    @Column(name = "FPassword", nullable = false, length = 100)
    private String FPassword;

    @Column(name = "FJob", nullable = false, length = 50)
    private String FJob;

    @Column(name = "FType", nullable = false)
    private Integer FType;

    @Column(name = "FPy", nullable = false, length = 50)
    private String FPy;

    @Column(name = "FWb", nullable = false, length = 50)
    private String FWb;

    @Column(name = "FExp", nullable = false, length = 200)
    private String FExp;

    @Column(name = "FMark", nullable = false)
    private Integer FMark;

    @Column(name = "FCreateDate", nullable = false, length = 8)
    private Date FCreateDate;

    @Column(name = "FCreateuserID", nullable = false)
    private Integer FCreateuserID;

    @Column(name = "FModifyDate", nullable = false, length = 8)
    private Date FModifyDate;

    @Column(name = "FModifyUserID", nullable = false)
    private Integer FModifyUserID;
}

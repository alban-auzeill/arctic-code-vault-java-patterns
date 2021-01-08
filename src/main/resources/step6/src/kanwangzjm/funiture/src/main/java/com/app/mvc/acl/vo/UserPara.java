package com.app.mvc.acl.vo; // (rank 1002) copied from https://github.com/kanwangzjm/funiture/blob/cc1382fec0a6b2dae7b1124a47686c6a378abfae/src/main/java/com/app/mvc/acl/vo/UserPara.java

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class UserPara {

    private Integer id;

    @NotNull(message = "用户名不可以为空")
    @Length(min = 1, max = 64, message = "用户名长度需要在64个字以内")
    private String username;

    @NotNull(message = "电话不可以为空")
    @Length(min = 1, max = 32, message = "电话长度需要在32个字以内")
    private String telephone;

    @NotNull(message = "邮箱不允许为空")
    @Length(min = 10, max = 32, message = "邮箱长度需要在32个字以内")
    @Email(message = "邮箱格式不对")
    private String mail;

    private Integer supplierId;

    private String managedSupplierIds;

    @NotNull(message = "必须选择用户所在部门")
    private Integer deptId;

    /**
     * @see com.app.mvc.acl.enums.Status
     */
    @NotNull(message = "必须指定用户的状态")
    @Min(value = -1, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;
    private String remark;

}

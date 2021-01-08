package cn.iocoder.mall.managementweb.controller.passport.dto; // (rank 734) copied from https://github.com/YunaiV/onemall/blob/671d4ad2461cd404c5d154d84115e12786edcb3f/management-web-app/src/main/java/cn/iocoder/mall/managementweb/controller/passport/dto/PassportLoginDTO.java

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ApiModel("管理登录 DTO")
@Data
@Accessors(chain = true)
public class PassportLoginDTO implements Serializable {

    @ApiModelProperty(value = "用户名", required = true, example = "yudaoyuanma")
    @NotEmpty(message = "登陆账号不能为空")
    @Length(min = 5, max = 16, message = "账号长度为 5-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "buzhidao")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

}

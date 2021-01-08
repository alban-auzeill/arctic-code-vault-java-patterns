package run.halo.app.model.params; // (rank 149) copied from https://github.com/halo-dev/halo/blob/ab74d8c28b5008bd614f015476bb2ad8b9f53ff6/src/main/java/run/halo/app/model/params/MailParam.java

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Journal query params.
 *
 * @author ryanwang
 * @date 2019/05/07
 */
@Data
public class MailParam {

    @NotBlank(message = "收件人不能为空")
    @Email(message = "邮箱格式错误")
    private String to;

    @NotBlank(message = "主题不能为空")
    private String subject;

    @NotBlank(message = "内容不能为空")
    private String content;
}

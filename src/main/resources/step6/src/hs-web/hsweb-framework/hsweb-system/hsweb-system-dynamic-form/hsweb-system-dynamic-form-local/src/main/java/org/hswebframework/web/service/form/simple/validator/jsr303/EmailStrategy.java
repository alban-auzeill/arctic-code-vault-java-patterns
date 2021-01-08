package org.hswebframework.web.service.form.simple.validator.jsr303; // (rank 83) copied from https://github.com/hs-web/hsweb-framework/blob/9b6f97417cbcb3f473bddc77ee7d133b76118e4c/hsweb-system/hsweb-system-dynamic-form/hsweb-system-dynamic-form-local/src/main/java/org/hswebframework/web/service/form/simple/validator/jsr303/EmailStrategy.java

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author zhouhao
 * @since 3.0.0-RC
 */
@Component
@Slf4j
public class EmailStrategy extends AbstractStrategy {

    public EmailStrategy() {
        addPropertyMapping(PropertyMapping.of("regexp", String.class));
    }

    @Override
    protected Class<Email> getAnnotationType() {
        return Email.class;
    }
}

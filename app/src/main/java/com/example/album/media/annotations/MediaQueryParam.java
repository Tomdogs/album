package com.example.album.media.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MediaQueryParam {
    String SORT_DESC = " DESC";
    String SORT_ASC = " ASC";

    String[] select() default {};

    String[] selectArgs() default {};

    String sortBy() default "date_added";

    String sortType() default " DESC";
}

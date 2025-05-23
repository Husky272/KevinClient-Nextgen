package kevin.font;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@Target(
        allowedTargets = {AnnotationTarget.FIELD}
)
@Retention(AnnotationRetention.RUNTIME)
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.FIELD})

public @interface FontDetails {
    String fontName();

    int fontSize() default -1;
}

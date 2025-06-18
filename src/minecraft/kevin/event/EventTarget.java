package kevin.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods as event targets.
 * Methods annotated with this will be called when the corresponding event is fired.
 * The ignoreCondition flag can be used to bypass any conditions that would normally prevent the event from being processed.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
    /**
     * Ignore condition
     * @return use your brain LOL
     */
    public boolean ignoreCondition() default false;
}

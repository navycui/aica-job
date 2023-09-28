package aicluster.framework.batch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchJob {
    public String jobName() default "BatchJob";
    public boolean loggable() default true;
    public boolean checkRunnable() default true;
}

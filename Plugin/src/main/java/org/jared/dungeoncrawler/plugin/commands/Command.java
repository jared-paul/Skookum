package org.jared.dungeoncrawler.plugin.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by JPaul on 8/18/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
    String[] aliases();

    String usage();

    String initializer();

    String description();

    String flags() default "";

    String help() default "";

    boolean nested() default false;

    int minArgs();

    int maxArgs();
}

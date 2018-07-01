package monri.com.terminalemulator.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by jasminsuljic on 08/11/2017.
 * Parkmatix
 */

@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface UserScope {
}

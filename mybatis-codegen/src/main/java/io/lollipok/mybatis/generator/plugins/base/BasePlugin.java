package io.lollipok.mybatis.generator.plugins.base;

import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public abstract class BasePlugin extends PluginAdapter {

  private static final String RUNTIME_MYBATIS3 = "MyBatis3";

  protected List<String> warnings;

  @Override
  public boolean validate(List<String> warnings) {
    this.warnings = warnings;

    String runtime = getContext().getTargetRuntime();

    if (!StringUtility.stringHasValue(runtime)) {
      return true;
    }

    if (RUNTIME_MYBATIS3.equalsIgnoreCase(runtime)) {
      return true;
    }

    try {
      Class<?> clazz = Class.forName(runtime);
      Object obj = clazz.newInstance();

      if (obj instanceof IntrospectedTable) {
        if (((IntrospectedTable) obj).getTargetRuntime().equals(TargetRuntime.MYBATIS3)) {
          return true;
        }
      }

    } catch (Exception ignored) {
    }

    warnings.add(String.format("The TargetRuntime must be %s", RUNTIME_MYBATIS3));
    return false;
  }
}

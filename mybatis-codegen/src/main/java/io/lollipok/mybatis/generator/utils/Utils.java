package io.lollipok.mybatis.generator.utils;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-30
 */
public final class Utils {

  private static final String PROP_ONLY_GENERATE_INSERT_SELECTIVE_METHOD =
      "onlyGenerateInsertSelectiveMethod";
  private static final String PROP_ONLY_GENERATE_UPDATE_SELECTIVE_METHOD =
      "onlyGenerateUpdateSelectiveMethod";

  private Utils() {}

  public static boolean onlyGenerateInsertSelectiveMethod(
      Context context, IntrospectedTable introspectedTable) {
    String onlyGenerateInsertSelectiveMethod =
        introspectedTable.getTableConfigurationProperty(PROP_ONLY_GENERATE_INSERT_SELECTIVE_METHOD);

    if (!StringUtility.stringHasValue(onlyGenerateInsertSelectiveMethod)) {
      onlyGenerateInsertSelectiveMethod =
          context.getProperty(PROP_ONLY_GENERATE_INSERT_SELECTIVE_METHOD);
    }

    return StringUtility.isTrue(onlyGenerateInsertSelectiveMethod);
  }

  public static boolean onlyGenerateUpdateSelectiveMethod(
      Context context, IntrospectedTable introspectedTable) {
    String onlyGenerateUpdateSelectiveMethod =
        introspectedTable.getTableConfigurationProperty(PROP_ONLY_GENERATE_UPDATE_SELECTIVE_METHOD);

    if (!StringUtility.stringHasValue(onlyGenerateUpdateSelectiveMethod)) {
      onlyGenerateUpdateSelectiveMethod =
          context.getProperty(PROP_ONLY_GENERATE_UPDATE_SELECTIVE_METHOD);
    }

    return StringUtility.isTrue(onlyGenerateUpdateSelectiveMethod);
  }
}

package io.lollipok.mybatis.generator.utils;

import java.util.Properties;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-30
 */
public final class PluginUtils {

  private static final String PROP_MARK_AS_DELETED_COLUMN = "markAsDeletedColumn";
  private static final String PROP_MARK_AS_DELETED_VALUE = "markAsDeletedValue";
  private static final String PROP_MARK_AS_UN_DELETED_VALUE = "markAsUnDeletedValue";

  private static final int DEFAULT_MARK_AS_DELETED_VALUE = 1;
  private static final int DEFAULT_MARK_AS_UN_DELETED_VALUE = 0;

  private PluginUtils() {}

  public static IntrospectedColumn getMarkAsDeletedColumn(
      Properties pluginProperties, IntrospectedTable introspectedTable) {
    String columnName =
        getProperty(pluginProperties, introspectedTable, PROP_MARK_AS_DELETED_COLUMN);

    if (StringUtility.stringHasValue(columnName)) {
      return introspectedTable.getColumn(columnName);
    }

    return null;
  }

  public static int getMarkAsDeletedValue(
      Properties pluginProperties, IntrospectedTable introspectedTable) {
    String markAsDeletedValue =
        getProperty(pluginProperties, introspectedTable, PROP_MARK_AS_DELETED_VALUE);

    if (StringUtility.stringHasValue(markAsDeletedValue)) {
      return Integer.parseInt(markAsDeletedValue);
    }

    return DEFAULT_MARK_AS_DELETED_VALUE;
  }

  public static int getMarkAsUnDeletedValue(
      Properties pluginProperties, IntrospectedTable introspectedTable) {
    String markAsUnDeletedValue =
        getProperty(pluginProperties, introspectedTable, PROP_MARK_AS_UN_DELETED_VALUE);

    if (StringUtility.stringHasValue(markAsUnDeletedValue)) {
      return Integer.parseInt(markAsUnDeletedValue);
    }

    return DEFAULT_MARK_AS_UN_DELETED_VALUE;
  }

  private static String getProperty(
      Properties pluginProperties, IntrospectedTable introspectedTable, String property) {
    String value = introspectedTable.getTableConfigurationProperty(property);

    if (!StringUtility.stringHasValue(value)) {
      value = pluginProperties.getProperty(property);
    }

    return value;
  }
}

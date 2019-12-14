/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.digimono.mybatis.generator.utils;

import org.mybatis.generator.api.CompositePlugin;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

/** @author yangyanju */
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

    Optional<IntrospectedColumn> introspectedColumn;
    if (StringUtility.stringHasValue(columnName)) {
      introspectedColumn = introspectedTable.getColumn(columnName);
    } else {
      introspectedColumn = Optional.empty();
    }

    return introspectedColumn.orElse(null);
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

  public static boolean hasPlugin(Context context, Class<? extends Plugin> pluginClass) {
    if (context == null) {
      return false;
    }

    boolean hasPlugin = false;
    CompositePlugin compositePlugin = (CompositePlugin) context.getPlugins();
    try {
      Object value = ReflectUtils.getValue(compositePlugin, CompositePlugin.class, "plugins");
      if (value instanceof List) {
        @SuppressWarnings("unchecked")
        List<Plugin> plugins = (List<Plugin>) value;
        hasPlugin = plugins.stream().anyMatch(plugin -> plugin.getClass().equals(pluginClass));
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    return hasPlugin;
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

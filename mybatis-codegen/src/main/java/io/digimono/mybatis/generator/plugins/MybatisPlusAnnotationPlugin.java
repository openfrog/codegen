package io.digimono.mybatis.generator.plugins;

import static io.digimono.mybatis.generator.constants.Constants.LINE_INDENT;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import io.digimono.mybatis.generator.constants.Constants.MyBatisPlus;
import io.digimono.mybatis.generator.plugins.base.BasePlugin;
import io.digimono.mybatis.generator.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

/** @author yangyanju */
public class MybatisPlusAnnotationPlugin extends BasePlugin {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private static final char UNDERLINE = '_';

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
      final String schema =
          introspectedTable.getTableConfigurationProperty(MyBatisPlus.TABLE_SCHEMA);
      final String resultMap =
          introspectedTable.getTableConfigurationProperty(MyBatisPlus.TABLE_RESULT_MAP);
      final String autoResultMap =
          introspectedTable.getTableConfigurationProperty(MyBatisPlus.TABLE_AUTO_RESULT_MAP);
      final String keepGlobalPrefix =
          introspectedTable.getTableConfigurationProperty(MyBatisPlus.TABLE_KEEP_GLOBAL_PREFIX);

      final String tableName = introspectedTable.getTableConfiguration().getTableName();
      final StringBuilder sb = new StringBuilder();
      sb.append("@TableName(");
      sb.append("value = \"").append(tableName).append("\"");

      if (StringUtility.stringHasValue(resultMap)) {
        sb.append(", ");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_INDENT);
        sb.append("resultMap = \"").append(resultMap).append("\"");
      }

      if (StringUtility.stringHasValue(schema)) {
        sb.append(", ");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_INDENT);
        sb.append("schema = \"").append(schema).append("\"");
      }

      if (StringUtility.stringHasValue(autoResultMap)) {
        sb.append(", ");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_INDENT);
        sb.append("autoResultMap = ").append(autoResultMap);
      }

      if (StringUtility.stringHasValue(keepGlobalPrefix)) {
        sb.append(", ");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_INDENT);
        sb.append("keepGlobalPrefix = ").append(keepGlobalPrefix);
      }

      sb.append(")");

      // don't need to do this for MYBATIS3_DSQL as that runtime already adds this annotation
      topLevelClass.addImportedType(
          new FullyQualifiedJavaType(
              "com.baomidou.mybatisplus.annotation.TableName")); // $NON-NLS-1$
      topLevelClass.addAnnotation(sb.toString()); // $NON-NLS-1$
    }
    return true;
  }

  @Override
  public boolean modelFieldGenerated(
      Field field,
      TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn,
      IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {
    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
      final List<AnnotationEntry> list = new ArrayList<>();
      final String actualColumnName = introspectedColumn.getActualColumnName();
      final TableConfiguration cnf = introspectedTable.getTableConfiguration();

      if (isTrue(cnf.getProperty(PropertyRegistry.TABLE_USE_ACTUAL_COLUMN_NAMES))) {
        list.add(new AnnotationEntry("value", actualColumnName, false, true));
      } else if (isTrue(cnf.getProperty(PropertyRegistry.TABLE_USE_COMPOUND_PROPERTY_NAMES))) {
        list.add(new AnnotationEntry("value", actualColumnName, false, true));
      } else {
        final ColumnOverride columnOverride = cnf.getColumnOverride(actualColumnName);
        if (columnOverride != null) {
          final String realJavaProperty = columnOverride.getJavaProperty();
          final String underlineName = camelToUnderline(realJavaProperty);
          final String typeHandler = columnOverride.getTypeHandler();

          if (!underlineName.equalsIgnoreCase(actualColumnName)) {
            list.add(new AnnotationEntry("value", actualColumnName, false, true));
          }

          final String addTypeHandler =
              introspectedTable.getTableConfigurationProperty(
                  MyBatisPlus.TABLE_FIELD_ADD_TYPE_HANDLER);

          if (Utils.toBoolean(addTypeHandler, true) && stringHasValue(typeHandler)) {

            list.add(new AnnotationEntry("typeHandler", typeHandler, true, false));
          }
        }
      }

      if (!list.isEmpty()) {
        final StringBuilder sb = new StringBuilder();
        sb.append("@TableField(");

        AnnotationEntry entry;
        for (int i = 0; i < list.size(); i++) {
          entry = list.get(i);

          if (i > 0) {
            sb.append(", ");
            sb.append(LINE_SEPARATOR);
            sb.append(LINE_INDENT);
            sb.append(LINE_INDENT);
          }

          if (entry.isString()) {
            sb.append(entry.getName()).append(" = \"").append(entry.getValue()).append("\"");
          } else {
            if (entry.isClazz()) {
              final int lastDotIndex = entry.getValue().lastIndexOf(".");
              final String simpleClassName = entry.getValue().substring(lastDotIndex + 1);

              sb.append(entry.getName()).append(" = ").append(simpleClassName).append(".class");
              topLevelClass.addImportedType(new FullyQualifiedJavaType(entry.getValue()));
            } else {
              sb.append(entry.getName()).append(" = ").append(entry.getValue());
            }
          }
        }

        sb.append(")");

        field.addAnnotation(sb.toString());
        topLevelClass.addImportedType(
            new FullyQualifiedJavaType("com.baomidou.mybatisplus.annotation.TableField"));
      }
    }

    return true;
  }

  private String camelToUnderline(String fieldName) {
    int len = fieldName.length();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      char c = fieldName.charAt(i);
      if (Character.isUpperCase(c) && i > 0) {
        sb.append(UNDERLINE);
      }
      sb.append(Character.toLowerCase(c));
    }
    return sb.toString();
  }

  static class AnnotationEntry {

    private String name;
    private String value;
    private boolean clazz;
    private boolean string;

    public AnnotationEntry(String name, String value, boolean clazz, boolean string) {
      this.name = name;
      this.value = value;
      this.clazz = clazz;
      this.string = string;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public boolean isClazz() {
      return clazz;
    }

    public void setClazz(boolean clazz) {
      this.clazz = clazz;
    }

    public boolean isString() {
      return string;
    }

    public void setString(boolean string) {
      this.string = string;
    }
  }
}

package io.digimono.mybatis.generator.plugins;

import io.digimono.mybatis.generator.plugins.base.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/** @author yangyanju */
public class MybatisTableNameAnnotationPlugin extends BasePlugin {

  private static final String LINE_INDENT = "    ";
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
      final String schema =
          introspectedTable.getTableConfigurationProperty("myBatisPlusTableSchema");
      final String resultMap =
          introspectedTable.getTableConfigurationProperty("myBatisPlusTableResultMap");
      final String autoResultMap =
          introspectedTable.getTableConfigurationProperty("myBatisPlusTableAutoResultMap");
      final String keepGlobalPrefix =
          introspectedTable.getTableConfigurationProperty("myBatisPlusTableKeepGlobalPrefix");

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
}

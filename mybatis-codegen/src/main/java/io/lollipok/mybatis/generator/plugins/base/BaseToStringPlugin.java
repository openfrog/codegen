package io.lollipok.mybatis.generator.plugins.base;

import static io.lollipok.mybatis.generator.constants.Constants.SERIAL_VERSION_UID_FIELD_NAME;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.List;
import java.util.Properties;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public abstract class BaseToStringPlugin extends BasePlugin {

  protected boolean useToStringFromRoot;

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
    useToStringFromRoot = isTrue(properties.getProperty("useToStringFromRoot"));
  }

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    return generateToString(introspectedTable, topLevelClass);
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    return generateToString(introspectedTable, topLevelClass);
  }

  @Override
  public boolean modelPrimaryKeyClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    return generateToString(introspectedTable, topLevelClass);
  }

  protected boolean generateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(FullyQualifiedJavaType.getStringInstance());
    method.setName("toString");
    if (introspectedTable.isJava5Targeted()) {
      method.addAnnotation("@Override");
    }

    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3_DSQL) {
      context
          .getCommentGenerator()
          .addGeneralMethodAnnotation(method, introspectedTable, topLevelClass.getImportedTypes());
    } else {
      context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    }

    return doGenerateToString(introspectedTable, topLevelClass, method);
  }

  protected boolean ignoreField(Field field) {
    String property = field.getName();
    return property.equalsIgnoreCase(SERIAL_VERSION_UID_FIELD_NAME);
  }

  protected abstract boolean doGenerateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass, Method method);
}

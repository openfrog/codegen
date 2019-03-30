package io.lollipok.mybatis.generator.plugins;

import io.lollipok.mybatis.generator.plugins.base.BasePlugin;
import io.lollipok.mybatis.generator.utils.PluginUtils;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class MarkAsDeletedByIdPlugin extends BasePlugin {

  private static final String STATEMENT_ID_MARK_AS_DELETED_BY_ID = "markAsDeletedById";

  private IntrospectedColumn introspectedColumn;
  private int markAsDeletedValue;
  private int markAsUnDeletedValue;

  @Override
  public void initialized(IntrospectedTable introspectedTable) {
    super.initialized(introspectedTable);

    Properties properties = getProperties();

    this.introspectedColumn = PluginUtils.getMarkAsDeletedColumn(properties, introspectedTable);
    this.markAsDeletedValue = PluginUtils.getMarkAsDeletedValue(properties, introspectedTable);
    this.markAsUnDeletedValue = PluginUtils.getMarkAsUnDeletedValue(properties, introspectedTable);

    if (this.introspectedColumn == null) {
      warnings.add("The logical delete column does not exist.");
    }
  }

  @Override
  public boolean clientGenerated(
      Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    if (this.introspectedColumn == null || !introspectedTable.hasPrimaryKeyColumns()) {
      return false;
    }

    Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setName(STATEMENT_ID_MARK_AS_DELETED_BY_ID);

    if (introspectedTable.getRules().generatePrimaryKeyClass()) {
      FullyQualifiedJavaType type =
          new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
      importedTypes.add(type);
      method.addParameter(new Parameter(type, "key"));
    } else {
      // no primary key class - fields are in the base class
      // if more than one PK field, then we need to annotate the
      // parameters
      // for MyBatis
      List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
      boolean annotate = introspectedColumns.size() > 1;
      if (annotate) {
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
      }
      StringBuilder sb = new StringBuilder();
      for (IntrospectedColumn introspectedColumn : introspectedColumns) {
        FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
        importedTypes.add(type);
        Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
        if (annotate) {
          sb.setLength(0);
          sb.append("@Param(\"");
          sb.append(introspectedColumn.getJavaProperty());
          sb.append("\")");
          parameter.addAnnotation(sb.toString());
        }
        method.addParameter(parameter);
      }
    }

    context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

    addMapperAnnotations(method);
    addExtraImports(interfaze);
    interfaze.addImportedTypes(importedTypes);
    interfaze.addMethod(method);

    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
    if (this.introspectedColumn == null || !introspectedTable.hasPrimaryKeyColumns()) {
      return false;
    }

    XmlElement rootElement = document.getRootElement();

    XmlElement answer = new XmlElement("update");
    answer.addAttribute(new Attribute("id", STATEMENT_ID_MARK_AS_DELETED_BY_ID));

    String parameterClass;
    if (introspectedTable.getRules().generatePrimaryKeyClass()) {
      parameterClass = introspectedTable.getPrimaryKeyType();
    } else {
      // PK fields are in the base class. If more than on PK
      // field, then they are coming in a map.
      if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
        parameterClass = "map";
      } else {
        parameterClass =
            introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
      }
    }

    answer.addAttribute(new Attribute("parameterType", parameterClass));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();

    sb.append("UPDATE ");
    sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));

    sb.setLength(0);
    sb.append("SET ")
        .append(introspectedColumn.getActualColumnName())
        .append(" = ")
        .append(markAsDeletedValue);

    answer.addElement(new TextElement(sb.toString()));

    boolean and = false;
    for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
      sb.setLength(0);
      if (and) {
        sb.append(" AND ");
      } else {
        sb.append("WHERE ");
        and = true;
      }

      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      answer.addElement(new TextElement(sb.toString()));
    }

    rootElement.addElement(answer);

    return true;
  }

  public void addMapperAnnotations(Method method) {}

  public void addExtraImports(Interface interfaze) {}
}

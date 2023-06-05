package io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static io.digimono.mybatis.generator.constants.Constants.STATEMENT_ID_UPDATE_BATCH_SELECTIVE;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyj
 * @since 2023/6/5
 */
public class UpdateBatchSelectiveElementGenerator extends AbstractXmlElementGenerator {

  private final List<String> ignoredColumns;

  public UpdateBatchSelectiveElementGenerator() {
    this(Collections.emptyList());
  }

  public UpdateBatchSelectiveElementGenerator(List<String> ignoredColumns) {
    super();
    this.ignoredColumns = ignoredColumns;
  }

  @Override
  public void addElements(XmlElement xmlElement) {
    XmlElement answer = new XmlElement("update");

    answer.addAttribute(new Attribute("id", STATEMENT_ID_UPDATE_BATCH_SELECTIVE));
    answer.addAttribute(new Attribute("parameterType", "java.util.List"));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("update ");
    sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());

    answer.addElement(new TextElement(sb.toString()));

    XmlElement element = new XmlElement("trim");
    element.addAttribute(new Attribute("prefix", "set"));
    element.addAttribute(new Attribute("suffixOverrides", ","));

    answer.addElement(element);

    final List<IntrospectedColumn> nonPrimaryKeyColumns =
        introspectedTable.getNonPrimaryKeyColumns();
    final List<IntrospectedColumn> columns;
    if (this.ignoredColumns != null && !this.ignoredColumns.isEmpty()) {
      columns =
          ListUtilities.removeGeneratedAlwaysColumns(nonPrimaryKeyColumns).stream()
              .filter(column -> !ignoredColumns.contains(column.getActualColumnName()))
              .collect(Collectors.toList());
    } else {
      columns = ListUtilities.removeGeneratedAlwaysColumns(nonPrimaryKeyColumns);
    }

    List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

    for (IntrospectedColumn introspectedColumn : columns) {
      sb.setLength(0);

      XmlElement columnTrimElement = new XmlElement("trim");
      columnTrimElement.addAttribute(
          new Attribute("prefix", introspectedColumn.getActualColumnName() + " = case"));
      columnTrimElement.addAttribute(new Attribute("suffix", "end,"));
      element.addElement(columnTrimElement);

      XmlElement columnElement = new XmlElement("foreach");
      columnElement.addAttribute(new Attribute("collection", "list"));
      columnElement.addAttribute(new Attribute("item", "item"));
      columnElement.addAttribute(new Attribute("index", "index"));
      columnTrimElement.addElement(columnElement);

      XmlElement isNotNullElement = new XmlElement("if");
      isNotNullElement.addAttribute(
          new Attribute("test", "item." + introspectedColumn.getJavaProperty() + " != null"));
      columnElement.addElement(isNotNullElement);
      sb.setLength(0);
      sb.append("when ");

      boolean and = false;
      for (IntrospectedColumn primaryKeyColumn : primaryKeyColumns) {
        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumn));
        if (and) {
          sb.append("  and ");
        } else {
          sb.append(" = ");
          and = true;
        }
        sb.append(
            MyBatis3FormattingUtilities.getParameterClause(primaryKeyColumn)
                .replace(
                    primaryKeyColumn.getJavaProperty(),
                    "item." + primaryKeyColumn.getJavaProperty()));
      }
      sb.append(" then ");
      sb.append(
          MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)
              .replace(
                  introspectedColumn.getJavaProperty(),
                  "item." + introspectedColumn.getJavaProperty()));
      isNotNullElement.addElement(new TextElement(sb.toString()));
    }

    boolean and = false;
    for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
      sb.setLength(0);
      if (and) {
        sb.append("  and "); // $NON-NLS-1$
      } else {
        sb.append("where "); // $NON-NLS-1$
        and = true;
      }

      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" in "); // $NON-NLS-1$
      answer.addElement(new TextElement(sb.toString()));

      XmlElement whereElement = new XmlElement("foreach"); // $NON-NLS-1$
      whereElement.addAttribute(new Attribute("collection", "list")); // $NON-NLS-1$ //$NON-NLS-2$
      whereElement.addAttribute(new Attribute("item", "item")); // $NON-NLS-1$ //$NON-NLS-2$
      whereElement.addAttribute(new Attribute("open", "(")); // $NON-NLS-1$ //$NON-NLS-2$
      whereElement.addAttribute(new Attribute("separator", ",")); // $NON-NLS-1$ //$NON-NLS-2$
      whereElement.addAttribute(new Attribute("close", ")")); // $NON-NLS-1$ //$NON-NLS-2$
      answer.addElement(whereElement);

      sb.setLength(0);
      sb.append(
          MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)
              .replace(
                  introspectedColumn.getJavaProperty(),
                  "item." + introspectedColumn.getJavaProperty()));
      whereElement.addElement(new TextElement(sb.toString()));
    }

    xmlElement.addElement(answer);
  }
}

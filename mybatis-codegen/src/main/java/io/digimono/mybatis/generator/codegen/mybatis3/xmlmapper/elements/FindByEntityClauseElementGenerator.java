package io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static io.digimono.mybatis.generator.constants.Constants.FIND_BY_ENTITY_CLAUSE_ID;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyj
 * @since 2023/11/17
 */
public class FindByEntityClauseElementGenerator extends AbstractXmlElementGenerator {

  public FindByEntityClauseElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {

    XmlElement answer = new XmlElement("sql");
    answer.addAttribute(new Attribute("id", FIND_BY_ENTITY_CLAUSE_ID));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();

    XmlElement dynamicElement = new XmlElement("where");
    answer.addElement(dynamicElement);

    for (IntrospectedColumn introspectedColumn :
        ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonBLOBColumns())) {
      XmlElement isNotNullElement = new XmlElement("if");
      sb.setLength(0);
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");
      isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
      dynamicElement.addElement(isNotNullElement);

      sb.setLength(0);
      sb.append("and ");
      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

      isNotNullElement.addElement(new TextElement(sb.toString()));
    }

    parentElement.addElement(answer);
  }
}

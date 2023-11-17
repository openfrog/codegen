package io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static io.digimono.mybatis.generator.constants.Constants.FIND_BY_ENTITY_CLAUSE_ID;
import static io.digimono.mybatis.generator.constants.Constants.STATEMENT_ID_FIND_BY_ENTITY;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyj
 * @since 2023/11/17
 */
public class FindByEntityElementGenerator extends AbstractXmlElementGenerator {

  public FindByEntityElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement xmlElement) {
    XmlElement answer = new XmlElement("select");

    answer.addAttribute(new Attribute("id", STATEMENT_ID_FIND_BY_ENTITY));

    FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
    answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));

    if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
      answer.addAttribute(
          new Attribute("resultMap", this.introspectedTable.getResultMapWithBLOBsId()));
    } else {
      answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
    }

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("select ");

    answer.addElement(new TextElement(sb.toString()));
    answer.addElement(this.getBaseColumnListElement());

    if (this.introspectedTable.hasBLOBColumns()) {
      answer.addElement(new TextElement(","));
      answer.addElement(this.getBlobColumnListElement());
    }

    sb.setLength(0);
    sb.append("from ");
    sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));

    answer.addElement(getSelectByEntityClauseElement());

    xmlElement.addElement(answer);
  }

  protected XmlElement getSelectByEntityClauseElement() {
    XmlElement answer = new XmlElement("include");
    answer.addAttribute(new Attribute("refid", FIND_BY_ENTITY_CLAUSE_ID));
    return answer;
  }
}

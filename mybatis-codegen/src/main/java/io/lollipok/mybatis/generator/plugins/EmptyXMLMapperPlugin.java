package io.lollipok.mybatis.generator.plugins;

import io.lollipok.mybatis.generator.plugins.base.BasePlugin;
import java.util.ArrayList;
import java.util.List;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class EmptyXMLMapperPlugin extends BasePlugin {

  @Override
  public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
      IntrospectedTable introspectedTable) {
    XmlElement answer = new XmlElement("mapper");
    String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
    answer.addAttribute(new Attribute("namespace", namespace));

    context.getCommentGenerator().addRootComment(answer);

    Document document =
        new Document(
            XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
    document.setRootElement(answer);

    GeneratedXmlFile emptyXml =
        new GeneratedXmlFile(
            document,
            calculateMyBatis3XmlMapperFileName(introspectedTable),
            introspectedTable.getMyBatis3XmlMapperPackage(),
            context.getSqlMapGeneratorConfiguration().getTargetProject(),
            true,
            context.getXmlFormatter());

    List<GeneratedXmlFile> list = new ArrayList<>();
    list.add(emptyXml);

    return list;
  }

  private String calculateMyBatis3XmlMapperFileName(IntrospectedTable introspectedTable) {
    TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
    StringBuilder sb = new StringBuilder();

    if (StringUtility.stringHasValue(tableConfiguration.getMapperName())) {
      String mapperName = tableConfiguration.getMapperName();
      int ind = mapperName.lastIndexOf('.');
      if (ind == -1) {
        sb.append(mapperName);
      } else {
        sb.append(mapperName.substring(ind + 1));
      }
      sb.append(".xml");
    } else {
      sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
      sb.append("Mapper.xml");
    }
    return sb.toString();
  }
}

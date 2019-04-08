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

package io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-04-08
 */
public abstract class BaseXmlElementGenerator extends AbstractXmlElementGenerator {

  public BaseXmlElementGenerator() {
    super();
  }

  protected XmlElement getSqlClauseElement(String refid) {
    XmlElement answer = new XmlElement("include");
    answer.addAttribute(new Attribute("refid", refid));
    return answer;
  }
}

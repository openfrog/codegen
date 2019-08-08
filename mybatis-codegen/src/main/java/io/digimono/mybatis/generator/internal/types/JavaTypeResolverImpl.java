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

package io.digimono.mybatis.generator.internal.types;

import java.sql.Types;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * @author yangyanju
 * @version 1.0
 */
public class JavaTypeResolverImpl extends JavaTypeResolverDefaultImpl {

  public JavaTypeResolverImpl() {
    super();
  }

  @Override
  protected FullyQualifiedJavaType overrideDefaultType(
      IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
    if (column.getJdbcType() == Types.TINYINT) {
      return new FullyQualifiedJavaType(Integer.class.getName());
    }

    return super.overrideDefaultType(column, defaultType);
  }
}

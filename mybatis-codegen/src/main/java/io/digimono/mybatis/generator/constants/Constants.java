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

package io.digimono.mybatis.generator.constants;

/** @author yangyanju */
public final class Constants {

  public static final String SERIAL_VERSION_UID_FIELD_NAME = "serialVersionUID";
  public static final String SERIAL_VERSION_UID_FIELD_VALUE = "1L";
  public static final String DEFAULT_XML_MAPPER_SUFFIX = "Mapper.generated.xml";
  public static final String CUSTOMIZED_XML_MAPPER_SUFFIX = ".generated.xml";

  public static final String INSERT_SELECTIVE_CLAUSE_ID = "Insert_Selective_Clause";
  public static final String UPDATE_SELECTIVE_CLAUSE_ID = "Update_Selective_Clause";

  public static final String PROP_ONLY_GENERATE_INSERT_SELECTIVE_METHOD =
      "onlyGenerateInsertSelectiveMethod";
  public static final String PROP_ONLY_GENERATE_UPDATE_SELECTIVE_METHOD =
      "onlyGenerateUpdateSelectiveMethod";
  public static final String PROP_GENERATE_EMPTY_JAVA_MAPPER = "generateEmptyJavaMapper";
  public static final String PROP_GENERATED_MAPPER_SUBPACKAGE = "generatedMapperSubpackage";
  public static final String PROP_GENERATED_MAPPER_SUFFIX = "generatedMapperSuffix";
  public static final String PROP_USE_DEFAULT_STATEMENT_ID = "useDefaultStatementId";

  public static final String STATEMENT_ID_MARK_AS_DELETED_BY_ID_1 = "markAsDeletedByPrimaryKey";
  public static final String STATEMENT_ID_MARK_AS_DELETED_BY_ID_2 = "markAsDeletedById";

  public static final class MyBatisPlus {

    public static final String TABLE_SCHEMA = "myBatisPlusTableSchema";
    public static final String TABLE_RESULT_MAP = "myBatisPlusTableResultMap";
    public static final String TABLE_AUTO_RESULT_MAP = "myBatisPlusTableAutoResultMap";
    public static final String TABLE_KEEP_GLOBAL_PREFIX = "myBatisPlusTableKeepGlobalPrefix";

    private MyBatisPlus() {}
  }

  private Constants() {}
}

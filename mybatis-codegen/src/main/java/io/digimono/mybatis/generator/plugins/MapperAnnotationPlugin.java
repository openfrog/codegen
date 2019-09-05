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

package io.digimono.mybatis.generator.plugins;

import io.digimono.mybatis.generator.utils.ReflectUtils;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.internal.PluginAggregator;

import java.util.ArrayList;
import java.util.List;

/** @author yangyanju */
public class MapperAnnotationPlugin extends org.mybatis.generator.plugins.MapperAnnotationPlugin {

  @Override
  public boolean validate(List<String> warnings) {
    PluginAggregator pluginAggregator = (PluginAggregator) context.getPlugins();

    try {
      Object value = ReflectUtils.getValue(pluginAggregator, PluginAggregator.class, "plugins");
      if (value instanceof ArrayList) {
        @SuppressWarnings("unchecked")
        List<Plugin> plugins = (ArrayList<Plugin>) value;

        boolean hasEmptyJavaMapperPlugin =
            plugins.stream().anyMatch(plugin -> plugin instanceof EmptyJavaMapperPlugin);

        if (hasEmptyJavaMapperPlugin) {
          return false;
        }
      }
    } catch (Exception ex) {
      warnings.add(
          String.format(
              "Plugin %s error. Msg: %s", MapperAnnotationPlugin.class.getName(), ex.getMessage()));

      return false;
    }

    return super.validate(warnings);
  }
}

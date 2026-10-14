/*
 *  Copyright 2026 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.teavm.javac;

import java.util.Map;
import java.util.function.Supplier;
import org.teavm.classlib.java.lang.ClassGenerator;
import org.teavm.classlib.java.lang.ObjectDependencyPlugin;
import org.teavm.classlib.java.lang.StringNativeDependency;
import org.teavm.classlib.java.lang.SystemDependencyPlugin;
import org.teavm.classlib.java.lang.reflect.ArrayDependencyPlugin;
import org.teavm.dependency.AbstractDependencyListener;
import org.teavm.dependency.DependencyAgent;
import org.teavm.dependency.DependencyPlugin;
import org.teavm.dependency.MethodDependency;
import org.teavm.dependency.PluggableDependency;
import org.teavm.jso.impl.JSNativeInjector;
import org.teavm.model.ValueType;

/**
 * Applies dependency plugins that class library methods declare with {@link PluggableDependency}.
 * <p>
 * Normally, dependency analyzer does this itself, instantiating plugin classes via reflection. When TeaVM
 * is compiled by TeaVM, which is the case here, it skips such annotations, so the plugins never run.
 * For example, without {@link ObjectDependencyPlugin}, result of {@code Object.clone()} does not get the
 * type of the cloned object, methods called on the clone are not linked, and code like {@code HashMap.clone()}
 * or {@code String.format("%.2f", x)} fails at runtime with "dereferencing a null pointer".
 * Since reflection is not available, plugins are enumerated explicitly. Plugins of {@code org.teavm.platform}
 * are omitted, as this API is not supported by WebAssembly GC backend.
 */
class PluggableDependencySupport extends AbstractDependencyListener {
    private static final Map<String, Supplier<DependencyPlugin>> PLUGINS = Map.of(
            ObjectDependencyPlugin.class.getName(), ObjectDependencyPlugin::new,
            ClassGenerator.class.getName(), ClassGenerator::new,
            StringNativeDependency.class.getName(), StringNativeDependency::new,
            SystemDependencyPlugin.class.getName(), SystemDependencyPlugin::new,
            ArrayDependencyPlugin.class.getName(), ArrayDependencyPlugin::new,
            JSNativeInjector.class.getName(), JSNativeInjector::new
    );

    @Override
    public void methodReached(DependencyAgent agent, MethodDependency method) {
        var annotation = method.getMethod().getAnnotations().get(PluggableDependency.class.getName());
        if (annotation == null) {
            return;
        }
        var pluginClassName = ((ValueType.Object) annotation.getValue("value").getJavaClass()).getClassName();
        var plugin = PLUGINS.get(pluginClassName);
        if (plugin != null) {
            plugin.get().methodReached(agent, method);
        }
    }
}

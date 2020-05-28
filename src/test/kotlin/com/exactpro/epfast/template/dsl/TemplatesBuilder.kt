/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.Reference
import com.exactpro.epfast.template.simple.Template
import com.exactpro.epfast.template.simple.Templates

class TemplatesBuilder internal constructor(val templates: Templates) {

    fun template(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: TemplateBuilder.() -> Unit) {
        templates.templates.add(TemplateBuilder(name, namespace, Template()).apply(block).template)
    }
}

fun templates(block: TemplatesBuilder.() -> Unit): Templates =
        TemplatesBuilder(Templates()).apply(block).templates

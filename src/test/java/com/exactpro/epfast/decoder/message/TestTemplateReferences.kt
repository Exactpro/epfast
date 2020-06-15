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

package com.exactpro.epfast.decoder.message

import com.exactpro.epfast.template.dsl.templates
import com.exactpro.epfast.template.simple.Reference
import com.exactpro.junit5.WithByteBuf
import io.netty.buffer.ByteBuf
import java.io.IOException
import org.assertj.core.api.Assertions.assertThat

class TestTemplateReferences {

    private val templates = templates {
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    isOptional = false
                }
                int32("int32_null_1") {
                    isOptional = true
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    isOptional = false
                }
                asciiString("ascii_null_1") {
                    isOptional = true
                }
            }
        }
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    isOptional = false
                }
                int32("int32_null_2") {
                    isOptional = true
                }
                asciiString("ascii_2") {
                    isOptional = false
                }
                templateRef {
                    name = "third template"
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
        template("third template") {
            auxiliaryId = "id_3"
            instructions {
                int32("int32_3") {
                    isOptional = false
                }
                int32("int32_null_3") {
                    isOptional = true
                }
                asciiString("ascii_3") {
                    isOptional = false
                }
                asciiString("ascii_null_3") {
                    isOptional = true
                }
            }
        }
    }

    @WithByteBuf(bytesString)
    @Throws(IOException::class)
    fun testTemplateRef(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templates.templates, Reference("first template", ""))

        val messages: MutableList<Any?> = mutableListOf()
        for (buffer in buffers) {
            messages.addAll(handler.process(buffer))
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField(Reference("int32_1"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_1"))).isEqualTo(0)
        assertThat(message.getField(Reference("int32_2"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_2"))).isEqualTo(0)
        assertThat(message.getField(Reference("ascii_2"))).isEqualTo("\u0000\u0000")
        assertThat(message.getField(Reference("int32_3"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_3"))).isEqualTo(0)
        assertThat(message.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
        assertThat(message.getField(Reference("ascii_null_3"))).isEqualTo("ABC")
        assertThat(message.getField(Reference("ascii_null_2"))).isEqualTo("ABC")
        assertThat(message.getField(Reference("ascii_1"))).isEqualTo("\u0000\u0000")
        assertThat(message.getField(Reference("ascii_null_1"))).isEqualTo("ABC")
    }

    companion object {
        const val bytesString = BufferStrings.MANDATORY_INT32_942755 + BufferStrings.OPTIONAL_INT32_0 +
                BufferStrings.MANDATORY_INT32_942755 + BufferStrings.OPTIONAL_INT32_0 + BufferStrings.ASCII_ZERO +
                BufferStrings.MANDATORY_INT32_942755 + BufferStrings.OPTIONAL_INT32_0 + BufferStrings.ASCII_ZERO +
                BufferStrings.ASCII_ABC + BufferStrings.ASCII_ABC + BufferStrings.ASCII_ZERO + BufferStrings.ASCII_ABC
    }
}

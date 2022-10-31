/*
 * Copyright (C) 2017 The ORT Project Authors (see <https://github.com/oss-review-toolkit/ort/blob/main/NOTICE>)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */

package org.ossreviewtoolkit.reporter

import java.io.File
import java.util.ServiceLoader
import java.util.SortedMap

import org.ossreviewtoolkit.model.OrtResult
import org.ossreviewtoolkit.model.config.PathExclude
import org.ossreviewtoolkit.model.config.ScopeExclude
import org.ossreviewtoolkit.utils.common.joinNonBlank

/**
 * A reporter that creates a human-readable report from a given [OrtResult].
 */
interface Reporter {
    companion object {
        private val LOADER = ServiceLoader.load(Reporter::class.java)

        /**
         * All [reporters][Reporter] available in the classpath, associated by their names.
         */
        val ALL: SortedMap<String, Reporter> by lazy {
            LOADER.iterator().asSequence().associateByTo(sortedMapOf(String.CASE_INSENSITIVE_ORDER)) { it.reporterName }
        }
    }

    /**
     * The name to use to refer to the reporter.
     */
    val reporterName: String

    /**
     * Generate a report for the provided [input] and write the generated file(s) to the [outputDir]. If and how the
     * [input] data is used depends on the specific reporter implementation, taking into account any format-specific
     * [options]. The list of generated report files is returned.
     */
    fun generateReport(
        input: ReporterInput,
        outputDir: File,
        options: Map<String, String> = emptyMap()
    ): List<File>
}

internal val PathExclude.description: String get() = joinNonBlank(reason.toString(), comment)

internal val ScopeExclude.description: String get() = joinNonBlank(reason.toString(), comment)

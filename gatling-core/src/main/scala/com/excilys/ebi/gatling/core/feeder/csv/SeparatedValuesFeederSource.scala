/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.core.feeder.csv

import scala.io.Source
import scala.tools.nsc.io.Path

import com.excilys.ebi.gatling.core.config.{ GatlingConfiguration, GatlingFiles }
import com.excilys.ebi.gatling.core.feeder.FeederSource
import com.excilys.ebi.gatling.core.util.FileHelper.{ COMMA_SEPARATOR, SEMICOLON_SEPARATOR, TABULATION_SEPARATOR }

object SeparatedValuesFeederSource {

	def csv(fileName: String, escapeChar: Option[String]): FeederSource = csv(GatlingFiles.dataDirectory / fileName, escapeChar)
	def csv(file: Path, escapeChar: Option[String]): FeederSource = new SeparatedValuesFeederSource(file, COMMA_SEPARATOR, escapeChar)

	def tsv(fileName: String, escapeChar: Option[String]): FeederSource = tsv(GatlingFiles.dataDirectory / fileName, escapeChar)
	def tsv(file: Path, escapeChar: Option[String]): FeederSource = new SeparatedValuesFeederSource(file, TABULATION_SEPARATOR, escapeChar)

	def ssv(fileName: String, escapeChar: Option[String]): FeederSource = ssv(GatlingFiles.dataDirectory / fileName, escapeChar)
	def ssv(file: Path, escapeChar: Option[String]): FeederSource = new SeparatedValuesFeederSource(file, SEMICOLON_SEPARATOR, escapeChar)
}

class SeparatedValuesFeederSource(file: Path, separator: String, escapeChar: Option[String] = None) extends FeederSource(file.name) {

	if (!file.exists) throw new IllegalArgumentException("file " + file + " doesn't exists")

	val data: IndexedSeq[Map[String, String]] = {

		val rawLines = Source.fromFile(file.jfile, GatlingConfiguration.configuration.simulation.encoding).getLines.map(_.split(separator))

		val lines = escapeChar.map { escape =>
			rawLines.map(_.map(_.stripPrefix(escape).stripSuffix(escape)))
		}.getOrElse(rawLines).toList

		val headers = lines.head

		lines.tail.map(line => (headers zip line).toMap[String, String]).toIndexedSeq
	}
}

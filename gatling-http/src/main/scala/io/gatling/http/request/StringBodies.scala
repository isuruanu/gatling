/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.excilys.com)
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
package io.gatling.http.request

import io.gatling.core.config.GatlingConfiguration.configuration
import io.gatling.core.session.{ Expression, Session }

object StringBodies {

	def buildExpression[T](expression: Expression[String], f: String => T): Expression[T] = (session: Session) =>
		expression(session).map(f)

	def asString(expression: Expression[String]): StringBody = new StringBody(expression)

	def asBytes(expression: Expression[String]): ByteArrayBody = {
		val bytes = buildExpression(expression, _.getBytes(configuration.core.encoding))
		new ByteArrayBody(bytes)
	}
}
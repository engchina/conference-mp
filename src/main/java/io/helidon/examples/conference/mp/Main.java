/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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
 */

package io.helidon.examples.conference.mp;

import java.io.IOException;
import java.util.logging.LogManager;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.PollingStrategies;
import io.helidon.microprofile.server.Server;

/**
 * Main method simulating trigger of main method of the server.
 */
public final class Main {

	/**
	 * Cannot be instantiated.
	 */
	private Main() {
	}

	/**
	 * Application main entry point.
	 * 
	 * @param args command line arguments
	 * @throws IOException if there are problems reading logging properties
	 */
	public static void main(final String[] args) throws IOException {

		// as we use default HTTP connection for Jersey client, we should set this as we
		// set the Authorization header
		// when propagating security
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

		setupLogging();

		Server server = startServer();

		System.out.println("http://localhost:" + server.port() + "/greet");
	}

	/**
	 * Start the server.
	 * 
	 * @return the created {@link Server} instance
	 */
	static Server startServer() {
		// Server will automatically pick up configuration from
		// microprofile-config.properties
		// and Application classes annotated as @ApplicationScoped
//		return Server.create().start();
		return Server.builder().config(buildConfig()).build().start();
	}

	/**
	 * Configure logging from logging.properties file.
	 */
	private static void setupLogging() throws IOException {
		// load logging configuration
		LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
	}

	private static Config buildConfig() {
		return Config.builder()
				.sources(
						ConfigSources.file("conf/dev-conference-mp.yaml").pollingStrategy(PollingStrategies::watch)
								.optional(),
						ConfigSources.classpath("application.yaml").optional(),
						ConfigSources.classpath("META-INF/microprofile-config.properties"))
				.build();
	}
}

package org.chris.ecjkseg.elasticsearch;

import org.elasticsearch.common.inject.AbstractModule;

public class RestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RestHandler.class).asEagerSingleton();
	}

}

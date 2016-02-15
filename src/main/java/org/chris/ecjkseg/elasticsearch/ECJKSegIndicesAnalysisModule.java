package org.chris.ecjkseg.elasticsearch;

import org.chris.ecjkseg.Dictionary;
import org.elasticsearch.common.inject.AbstractModule;

public class ECJKSegIndicesAnalysisModule extends AbstractModule {

	@Override
	protected void configure() {
        Dictionary dic = new Dictionary(false);
        dic.loadDicFolder(AnalysisECJKSegPlugin.getDicFolder());
		super.bind(Dictionary.class).toInstance(dic);
		
		super.bind(ECJKSegIndicesAnalysis.class).asEagerSingleton();		
	}
}

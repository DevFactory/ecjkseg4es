package org.chris.ecjkseg.elasticsearch;

import org.apache.lucene.analysis.Tokenizer;
import org.chris.ecjkseg.Dictionary;
import org.chris.ecjkseg.analysis.ECJKAnalyzer;
import org.chris.ecjkseg.analysis.ECJKTokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

public class ECJKSegIndicesAnalysis extends AbstractComponent {
    @Inject
    public ECJKSegIndicesAnalysis(final Settings settings,
                                IndicesAnalysisService indicesAnalysisService,Environment env,
                                final Dictionary dic) {
        super(settings);
        
        //final Dictionary dic = new Dictionary(false);
        //dic.loadDicFolder(AnalysisECJKSegPlugin.getDicFolder());
        
        indicesAnalysisService.analyzerProviderFactories().put("ecjk",
                new PreBuiltAnalyzerProviderFactory("ecjk", AnalyzerScope.GLOBAL,
                        new ECJKAnalyzer(dic)));
        
        indicesAnalysisService.tokenizerFactories().put("ecjk",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "ecjk";
                    }

                    @Override
                    public Tokenizer create() {
                        return new ECJKTokenizer(dic);
                    }
                }));
    }

}

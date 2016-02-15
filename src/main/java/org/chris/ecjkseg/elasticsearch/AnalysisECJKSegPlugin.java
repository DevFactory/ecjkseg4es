package org.chris.ecjkseg.elasticsearch;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.chris.ecjkseg.Dictionary;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;

import com.google.common.collect.Lists;

public class AnalysisECJKSegPlugin extends Plugin {
	public static final String CONFIG_FOLDER = "config";
	public static final String PLUGIN_NAME = "analysis-ecjkseg";
	
    private final Settings settings;
    
    @Inject
    public AnalysisECJKSegPlugin(Settings settings) {
        this.settings = settings;
    }
    
    @Override public String name() {
        return PLUGIN_NAME;
    }

    @Override public String description() {
        return "analyzer ecjk";
    }

    @Override
    public Collection<Module> nodeModules() {
    	Collection<Module> modules = Lists.newArrayList(); 
        modules.add(new ECJKSegIndicesAnalysisModule());
        modules.add(new RestModule());
        return modules;
    }
	public static File getDicFolder() {
		return PathUtils.get(
				new File(Dictionary.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent(), 
				AnalysisECJKSegPlugin.CONFIG_FOLDER,AnalysisECJKSegPlugin.PLUGIN_NAME)
				.toFile();
	}
}

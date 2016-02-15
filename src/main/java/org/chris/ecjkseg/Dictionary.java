package org.chris.ecjkseg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;

public class Dictionary{
	
	public static final String DIC_LIST_FILE = "/diclist.txt";
	public static final Double MISSING_WORD_MF = 1.0;
	
    private PatriciaTrie<String, Double> trie;
    
    private int wordsCount = 0;
    private int maxLength = 0;//call this.remove() will not change this maxLength!

    public Dictionary(){
    	this(true);    	
    }
    public Dictionary(boolean loadDefault){
    	trie = new PatriciaTrie<String, Double>(StringKeyAnalyzer.CHAR);
    	if(loadDefault){
        	for(String dicfile : getDicList()){
        		this.loadDicFile(dicfile);
        	}
    	}
    }
    
    private static List<String> getDicList(){
    	LinkedList<String> rl = new LinkedList<>();
    	try(BufferedReader br =  new BufferedReader(new InputStreamReader(Dictionary.class.getResourceAsStream(DIC_LIST_FILE),"UTF-8"))){
    		String line = null;
    		while((line = br.readLine()) != null){
    			line = line.trim();
    			if(line.length() > 0){
    				if(!line.startsWith("/"))
    					line = "/" + line;
    				rl.add(line);
    			}
    		}
    	}catch(IOException ex){
            ex.printStackTrace();
        }
    	return rl;
    }

	private static File[] listWordsFiles(File folder) {
		return folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".dic");
			}
		});
	}
	public void loadDicFolder(File folder){
		File[] files = listWordsFiles(folder);
		if(files == null)
			return;
		for(File file : files){
			this.loadDicFile(file);
		}
	}
	public void loadDicFile(File file){
		InputStream is;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		this.loadDicFile(is);
	}

    public void loadDicFile(String filename){
    	InputStream is = Dictionary.class.getResourceAsStream(filename);
    	if(is == null){
    		new FileNotFoundException("dic file: "+filename+" not found! ").printStackTrace();
    		return;
    	}
        this.loadDicFile(is);
    }
    public void loadDicFile(InputStream is){
    	if(is == null)
    		return;
        try(BufferedReader br =  new BufferedReader(new InputStreamReader(is,"UTF-8"))){
            String line = null;
            while((line = br.readLine()) != null) {
                String[] la = line.split("\\s+");
                switch(la.length){
                case 0:
                	break;
                case 1:
                	this.putWord(la[0],MISSING_WORD_MF);
                	break;
                default:
                    this.putWord(la[0],Double.parseDouble(la[1]));
                    break;
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public boolean containsWord(String w){
    	return this.trie.containsKey(w);
    }

    public Double getWord(String w){
        return this.trie.get(w);
    }
    public Double putWord(String w,Double freq){
    	wordsCount++;
    	if(w.length() > this.maxLength)
    		this.maxLength = w.length();
        return this.trie.put(w,freq);
    }
    public Double remove(String w){
    	wordsCount--;
        return this.trie.remove(w);
    }
    public SortedMap<String,Double> prefixMap(String word){
        return this.trie.prefixMap(word);
    }
    public int getMaxLength(){
    	return this.maxLength;
    }
    public int getWordsCount(){
    	return this.wordsCount;
    }
}
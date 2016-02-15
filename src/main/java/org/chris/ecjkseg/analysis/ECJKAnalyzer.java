package org.chris.ecjkseg.analysis;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.chris.ecjkseg.Dictionary;

public class ECJKAnalyzer extends Analyzer {
	private Dictionary dic;
	public ECJKAnalyzer(){
		super();
		this.dic = new Dictionary();
	}
	public ECJKAnalyzer(Dictionary dic){
		super();
		this.dic = dic;
	}
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer ecjk = new ECJKTokenizer(this.dic);
		//return new TokenStreamComponents(ecjk,new LowerCaseFilter(ecjk));
		return new TokenStreamComponents(ecjk);
	}

	public static void main(String[] args) throws IOException {
		Analyzer analyzer = new ECJKAnalyzer(); // or any other analyzer
		TokenStream ts = analyzer.tokenStream("myfield", new StringReader("Some text goes here"));
		OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);

		try {
			ts.reset(); // Resets this stream to the beginning. (Required)
			while (ts.incrementToken()) {
				// Use AttributeSource.reflectAsString(boolean)
				// for token stream debugging.
				System.out.println("token: " + ts.reflectAsString(true));

				System.out.println("token start offset: " + offsetAtt.startOffset());
				System.out.println("  token end offset: " + offsetAtt.endOffset());
			}
			ts.end(); // Perform end-of-stream operations, e.g. set the final
						// offset.
		} finally {
			ts.close(); // Release resources associated with this stream.
		}
		analyzer.close();
	}
}

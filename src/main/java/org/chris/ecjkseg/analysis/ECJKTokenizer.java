package org.chris.ecjkseg.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.chris.ecjkseg.Dictionary;
import org.chris.ecjkseg.ECJKSeg;
import org.chris.ecjkseg.Word;

public class ECJKTokenizer extends Tokenizer {
	
	private CharTermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;
	private PositionIncrementAttribute posIncrAtt;
	
	private int finalOffset;
	
	private ECJKSeg ecjkseg;
	
	public ECJKTokenizer(){
		super();
		//
		this.termAtt = addAttribute(CharTermAttribute.class);
		this.offsetAtt = addAttribute(OffsetAttribute.class);
		this.typeAtt = addAttribute(TypeAttribute.class);
		this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		//
		this.ecjkseg = new ECJKSeg();
	}
	public ECJKTokenizer(Dictionary dic){
		super();
		//
		this.termAtt = addAttribute(CharTermAttribute.class);
		this.offsetAtt = addAttribute(OffsetAttribute.class);
		this.typeAtt = addAttribute(TypeAttribute.class);
		this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		//
		this.ecjkseg = new ECJKSeg(dic);
	}
	
	public void reset() throws IOException {
        super.reset();
		this.ecjkseg.setReader(super.input);
		this.finalOffset = 0;
	}

	@Override
	public final boolean incrementToken() throws IOException {
		super.clearAttributes();
		Word word = this.ecjkseg.nextWord();
		if(word != null) {
			this.termAtt.append(word.getText());
			this.offsetAtt.setOffset(word.getStartOffset(), word.getEndOffset()+1);//ECJKSeg use inclusive endoffset,lucene use disclusive end.
			this.typeAtt.setType(word.getType().toString());
			this.posIncrAtt.setPositionIncrement(1);
			//
			this.finalOffset = word.getEndOffset()+1;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void end() throws IOException{
		super.end();
		this.offsetAtt.setOffset(this.finalOffset, this.finalOffset);
	}

}

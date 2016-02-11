package org.chris.cnseg;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ECJKSeg {
	public static final LetterFilter letterFilter = new LetterFilter();
	//
	private Dictionary dic;
	//
	private StandardSeg sdseg;
	private MMSeg mmseg;
	//
	private int whichNext = 0; //used by next()
	
	public ECJKSeg(){
		dic = new Dictionary(true);
		mmseg = new MMSeg(dic);
		sdseg = new StandardSeg();
	}
	public void setReader(Reader reader) throws IOException{
		this.whichNext = 0;
		sdseg.setReader(reader);
	}
	
	public Word next(WordFilter... wfs) throws IOException{
		Word w = this.next();
		if(w == null)
			return null;
		for(WordFilter wf : wfs){
			if(!wf.accept(w)){
				return next(wfs);
			}
		}
		return w;
	}
	
	public Word nextWord() throws IOException{
		return next(letterFilter);
	}
	
	public Word next() throws IOException{
		Word w = null;
		if(this.whichNext == 0){
			w = sdseg.next();
			if(w == null)
				return null;
			if(isCJKWord(w)){
				mmseg.setSentence(w.getText(),w.getStartOffset());
				Word w1 = mmseg.next();
				assert w1 != null;
				this.whichNext = 1;
				return w1;
			}
			return w;
		}else{
			w = mmseg.next();
			if(w == null){
				this.whichNext = 0;
				return next();
			}
			return w;
		}
	}
	
	public void close() throws IOException{
		sdseg.close();
	}
	
	public static boolean isCJKWord(Word w){
		return isCJK(w.getText().charAt(0));
	}
	
	public static boolean isCJK(char c){
		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}
	public static boolean isCJK(int c){
		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}
	public static class LetterFilter implements WordFilter{
		@Override
		public boolean accept(Word w) {
			if (Character.isLetterOrDigit(w.getText().codePointAt(0)))
                return true;
			return false;
		}		
	}
	
	public static void main(String[] args) throws IOException{
		ECJKSeg seg = new ECJKSeg();
		String s = "如果纪勤不是一个合格的主席，那肯定是因为有人忘了通知他。我的用户名是aa885,我的邮箱是9999@gmail.com.";
		Reader r = new StringReader(s);
		seg.setReader(r);
		Word w;
		while((w = seg.nextWord()) != null){
			System.out.println(w);
		}
		seg.close();
	}

}

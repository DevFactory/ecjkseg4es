package org.chris.ecjkseg;

public class Word{
    private String text;//never null once inited, text.length() > 0
    private int startOffset;
    private int endOffset;
    private Type type; //when you set this.text, you must set this.type at the same time!
    //
    private int length;
    
    public enum Type { 
        STD_DIGIT("standard_digit"), STD_LETTER("standard_letter"),STD_OTHERS("standard_others"),CJK_WORD("cjk_word"); 
        private String type; 
        private Type(String type) { 
            this.type = type; 
        } 
        
        @Override 
        public String toString(){ 
            return type; 
        } 
    } 

    public Word(String text,int startOffset,int endOffset){
        this.text = text;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.length = endOffset - startOffset + 1;
        this.type = getType(text);
    }
    
    public Word(int startOffset,int endOffset){
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.length = endOffset - startOffset + 1;
    }
    public Type getType(){
    	return this.type;
    }
    public int getLength(){
    	return this.length;
    }
    public String getText(){
        return this.text;
    }
    public void setTextIfNull(String sen){
    	if(this.text == null){
    		this.text = sen.substring(this.startOffset,this.endOffset+1);
    		this.type = getType(this.text);
    	}
    }
    public String getOrSetText(String sen){
    	this.setTextIfNull(sen);
    	return this.text;
    }
    public int getStartOffset(){
        return this.startOffset;
    }
    public int getEndOffset(){
        return this.endOffset;
    }
    public void addSenIndex(int index){
    	this.startOffset += index;
    	this.endOffset += index;
    }
    public static Type getType(char cha){
    	switch(Character.getType(cha)){
    	case Character.UPPERCASE_LETTER:
    	case Character.LOWERCASE_LETTER:
    	case Character.TITLECASE_LETTER:
    	case Character.MODIFIER_LETTER:
    	case Character.OTHER_LETTER:
    		if(Character.UnicodeBlock.of(cha) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
    			return Type.CJK_WORD;
    		return Type.STD_LETTER;
    	case Character.DECIMAL_DIGIT_NUMBER:
    		return Type.STD_DIGIT;
    	default:
    		return Type.STD_OTHERS;
    	}
    }
    public static Type getType(int cha){
    	switch(Character.getType(cha)){
    	case Character.UPPERCASE_LETTER:
    	case Character.LOWERCASE_LETTER:
    	case Character.TITLECASE_LETTER:
    	case Character.MODIFIER_LETTER:
    	case Character.OTHER_LETTER:
    		if(Character.UnicodeBlock.of(cha) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
    			return Type.CJK_WORD;
    		return Type.STD_LETTER;
    	case Character.DECIMAL_DIGIT_NUMBER:
    		return Type.STD_DIGIT;
    	default:
    		return Type.STD_OTHERS;
    	}
    }
    public static Type getType(String s){
    	return getType(s.codePointAt(0));
    }
    
    @Override
    public String toString(){
    	return "text: "+this.text+" startOffset: "+this.startOffset+" endOffset: "+this.endOffset+" type: "+this.type;
    }

}
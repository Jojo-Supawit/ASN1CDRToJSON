package decoder;

import java.math.BigInteger;
import java.util.HashMap;

import decoder.Translator.Translate;
import index.Index;

public class TranslationMapNode {

	private String fieldName = "Unknown";
	private HashMap<BigInteger, TranslationMapNode> translationMap = new HashMap<BigInteger, TranslationMapNode>();
	private Translate translator;
	
	public TranslationMapNode() {
		
	}
	
	public TranslationMapNode(String fieldName) {
		this.setFieldName(fieldName);
	}
	
	public TranslationMapNode(HashMap<BigInteger, TranslationMapNode> translationMap) {
		this.setTranslationMap(translationMap);
	}
	
	public TranslationMapNode(String fieldName, HashMap<BigInteger, TranslationMapNode> translationMap) {
		this.setFieldName(fieldName);
		this.setTranslationMap(translationMap);
	}
	
	public TranslationMapNode(Translate translator) {
		this.setTranslator(translator);
	}
	
	public TranslationMapNode(String fieldName, Translate translator) {
		this.setFieldName(fieldName);
		this.setTranslator(translator);
	}
	
	public TranslationMapNode getChild(BigInteger tagNumber) {
		if(!translationMap.containsKey(tagNumber)) return null;
		return translationMap.get(tagNumber);
	}
	
	public String translate(byte[] rawData, Index index) {
		if(translator == null) return Translator.DEFAULTTRANSLATOR.translate(rawData, index);
		return translator.translate(rawData, index);
	}

	public void setTranslationMap(HashMap<BigInteger, TranslationMapNode> translationMap) {
		this.translationMap = translationMap;
	}

	public void setTranslator(Translate translator) {
		this.translator = translator;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}

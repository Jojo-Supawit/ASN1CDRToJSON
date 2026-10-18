package decoder;

import java.math.BigInteger;
import java.util.Vector;

import file.Container;
import index.Index;
import index.IndexNode;
import index.Tag;

public class Controller {

	private TranslationMapNode defaultMap = null;
	
	public Controller() {
		
	}
	
	public Controller(TranslationMapNode defaultMap) {
		this.defaultMap = defaultMap;
	}
	
	public String decode(Container container) {
		//Process...
		return sampleDecode(container);
	}
	
	public String sampleDecode(Container container) {
		String output = "[\n";
		IndexNode rootNode = Extracter.extractNode(container.getRawData(), new Index(0, new Tag((byte) 0, true, BigInteger.ZERO), container.getRawData().length, 0));
		Vector<String> childData = new Vector<String>(rootNode.getChildNodes().size());
		int counter = 0;
		for(IndexNode node: rootNode.getChildNodes()) {
			childData.add(fieldDecode(container.getRawData(), node, ((defaultMap != null)? defaultMap.getChild(node.getTag().getTagNumber()): null), counter, 1));
			counter++;
		}
		output += String.join(",\n", childData) + "\n]";
		return output;
	}
	
	private String fieldDecode(byte[] rawData, IndexNode index, TranslationMapNode translationMap, int id, int layerNumber) {
		String tab = "\t".repeat(layerNumber);
		String output = tab + "{\n";
		output += tab + "\t\"id\":" + id + ",\n" 
				+ tab + "\t\"classId\":" + index.getTag().getClassID() + ",\n"
				+ tab + "\t\"constructed\":" + index.getTag().isConstructed() + ",\n"
				+ tab + "\t\"tagNumber\":" + index.getTag().getTagNumber().toString() + ",\n"
				+ tab + "\t\"name\":\"" + ((translationMap != null)? translationMap.getFieldName(): "Unknown") + "\",\n"
				+ tab + "\t\"data\":\n"
				+ tab + "\t[\n";
		if(index.getTag().isConstructed()) {
			Vector<String> childData = new Vector<String>(index.getChildNodes().size());
			int counter = 0;
			for(IndexNode childNode: index.getChildNodes()) {
				childData.add(fieldDecode(rawData, childNode, ((translationMap != null)? translationMap.getChild(childNode.getTag().getTagNumber()): null), counter, layerNumber + 2));
				counter++;
			}
			output += String.join(",\n", childData);
		} else {
			output += tab + "\t\t" + ((translationMap != null)? translationMap.translate(rawData, index): Translator.DEFAULTTRANSLATOR.translate(rawData, index));
		}
		output += "\n" + tab +"\t]\n" + tab + "}";
		return output;
	}
	
}

package decoder;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Vector;

import index.Index;
import index.IndexNode;
import index.Tag;

public class Extracter {
	public static IndexNode extractNode(byte[] rawData, Index index) {
		IndexNode node = new IndexNode(index);
		if(node.getTag().isConstructed()) {
			for(Index i: extracts(rawData, node.getValueStartIndex(), node.getLength())) {
				IndexNode childNode = extractNode(rawData, i);
				childNode.setParent(node);
				node.addChild(childNode);
			}
		}
		return node;
	}
	
	public static Collection<?extends Index> extracts(byte[] rawData, int startIndex, int dataSize) {
		int nextIndex = startIndex;
		Vector<Index> indices = new Vector<Index>();
		while(nextIndex < startIndex + dataSize) {
			Index index = extract(rawData, nextIndex);
			indices.add(index);
			nextIndex = index.nextPos();
		}
		return indices;
	}
	
	public static Index extract(byte[] rawData, int startIndex) {
		int tagTemp = rawData[startIndex];
		if(tagTemp < 0) tagTemp += 256;
		Tag tag;
		int lengthStartIndex;
		if(tagTemp%32 == 31) {
			BigInteger tagNumber = BigInteger.ZERO;
			boolean hasNext = true;
			int offset = 1;
			while(hasNext) {
				tagNumber = tagNumber.shiftLeft(7);
				tagNumber = tagNumber.add(BigInteger.valueOf((rawData[startIndex + offset] < 0)? rawData[startIndex + offset] + 128 : rawData[startIndex + offset]));
				hasNext = rawData[startIndex + offset] < 0;
				offset++;
			}
			tag = new Tag((byte) (tagTemp/64), (tagTemp/32)%2 == 1, tagNumber);
			lengthStartIndex = startIndex + offset;
		} else {
			tag = new Tag((byte) (tagTemp/64), (tagTemp/32)%2 == 1, BigInteger.valueOf(tagTemp%32));
			lengthStartIndex = startIndex + 1;
		}
		int length;
		int valueStartIndex;
		if(rawData[lengthStartIndex] < 0) {
			int lengthSize = rawData[lengthStartIndex] + 128;
			length = 0;
			int i = 1;
			for(i = 1; i <= lengthSize; i++) {
				length <<= 8;
				length += (rawData[lengthStartIndex + i] < 0)? rawData[lengthStartIndex + i] + 256 : rawData[lengthStartIndex + i];
			}
			valueStartIndex = lengthStartIndex + i;
		} else {
			length = rawData[lengthStartIndex];
			valueStartIndex = lengthStartIndex + 1;
		}
		return new Index(startIndex, tag, length, valueStartIndex);
	}
}

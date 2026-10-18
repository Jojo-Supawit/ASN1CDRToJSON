package index;

import java.util.Vector;

public class IndexNode extends Index{
	
	private IndexNode parentNode;
	private Vector<IndexNode> childNodes = new Vector<IndexNode>();
	
	public IndexNode(int startIndex, Tag tag, int length, int valueStartIndex) {
		super(startIndex, tag, length, valueStartIndex);
	}
	
	public IndexNode(Index index) {
		super(index.startIndex, index.tag, index.length, index.valueStartIndex);
	}
	
	public IndexNode getParent() {
		return this.parentNode;
	}
	
	public void setParent(IndexNode parent) {
		this.parentNode = parent;
	}
	
	public void addChild(IndexNode child) {
		this.childNodes.add(child);
	}
	
	public void clearChildren() {
		this.childNodes.clear();
	}
	
	public IndexNode getChild(int i) {
		if(i >= childNodes.size()) return null;
		return childNodes.get(i);
	}
	
	public IndexNode popChild(int i) {
		if(i >= childNodes.size()) return null;
		IndexNode output = childNodes.get(i);
		childNodes.remove(i);
		return output;
	}

	public Vector<IndexNode> getChildNodes() {
		return childNodes;
	}
	
}

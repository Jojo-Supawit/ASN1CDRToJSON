package index;

public class Index {
	
	protected int startIndex;
	protected String name = "Unknown";
	protected Tag tag;
	protected int length;
	protected int valueStartIndex;
	
	public Index(int startIndex, Tag tag, int length, int valueStartIndex) {
		this.startIndex = startIndex;
		this.tag = tag;
		this.length = length;
		this.valueStartIndex = valueStartIndex;
	}
	
	public int nextPos() {
		return this.valueStartIndex + this.length;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public Tag getTag() {
		return tag;
	}

	public int getLength() {
		return length;
	}

	public int getValueStartIndex() {
		return valueStartIndex;
	}
}
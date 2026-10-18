package index;

import java.math.BigInteger;
import java.util.Objects;

public class Tag {
	private byte classID;
	private boolean constructed;
	private BigInteger tagNumber;
	
	public Tag(byte classID, boolean constructed, BigInteger tagNumber) {
		this.classID = classID;
		this.constructed = constructed;
		this.tagNumber = tagNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj.getClass() != this.getClass()) return false;
		Tag other = (Tag) obj;
		if(this.classID != other.classID) return false;
		if(this.constructed != other.constructed) return false;
		if(!(this.tagNumber.equals(other.tagNumber))) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(classID, constructed, tagNumber);
	}

	public byte getClassID() {
		return classID;
	}

	public boolean isConstructed() {
		return constructed;
	}

	public BigInteger getTagNumber() {
		return tagNumber;
	}
	
	
}

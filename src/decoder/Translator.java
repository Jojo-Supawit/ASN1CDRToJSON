package decoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import index.Index;

public class Translator {
	
	interface Translate {
		String translate(byte[] rawData, Index index);
	}
	
	public static final Translate HEXTRANSLATOR = (byte[] rawData, Index index) -> "\"" + HexFormat.of().formatHex(rawData, index.getValueStartIndex(), index.getValueStartIndex() + index.getLength()).toUpperCase() + "\"";

	public static final Translate INTTRANSLATOR = (byte[] rawData, Index index) -> (new BigInteger(1, rawData, index.getValueStartIndex(), index.getLength())).toString();
	
	public static final Translate BOOLEANTRANSLATOR = (byte[] rawData, Index index) -> ((new BigInteger(1, rawData, index.getValueStartIndex(), index.getLength())).equals(BigInteger.ZERO))? "false" : "true";
	
	public static final Translate UTF8STRINGTRANSLATOR = (byte[] rawData, Index index) -> "\"" + new String(rawData, index.getValueStartIndex(), index.getLength(), StandardCharsets.UTF_8) + "\"";
	
	public static final Translate NULLTRANSLATOR = (byte[] rawData, Index index) -> "null";
	
	public static final Translate DEFAULTTRANSLATOR = HEXTRANSLATOR;
}

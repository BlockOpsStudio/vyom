package studio.blockops.vyom.core.utils;



import org.bouncycastle.util.encoders.Hex;
// Please delete this class after unit test class
public class RLPTest {
	public static void main(String[] args) {
		int i = 1024;
		String str = "dog";
		String str1 = "cat";
		String result05 = "g0";
		String[] list = new String[] { "cat", "dog" };
		Object[] test = new Object[] { new Object[] { new Object[] {}, new Object[] {} }, new Object[] {} };
		Object[] test2 = new Object[] { new Object[] {}, new Object[] { new Object[] {} },
				new Object[] { new Object[] {}, new Object[] { new Object[] {} } } };
	
		byte[] intI = RLP.encode(i);
		System.out.println(RLP.byteAryToHex(intI));
		
		byte[] strb1 = RLP.encode(str1);
		System.out.println(RLP.byteAryToHex(strb1));
		
		byte[] strb = RLP.encode(str);
		System.out.println(RLP.byteAryToHex(strb));
		
		
		byte[] v = RLP.encode(list);
		System.out.println(RLP.byteAryToHex(v));
		
		
		System.out.println("-------");
		byte[] listoflist = RLP.encode(test);
		System.out.println(RLP.byteAryToHex(listoflist));
		
		
		
		System.out.println("-------");
		byte[] listlll = RLP.encode(test2);
		System.out.println(RLP.byteAryToHex(listlll));
		
		byte [] b = Hex.decode("83646f67");
		
		byte [] b1  = (byte[]) RLP.decode(b, 0).getDecoded();
		System.out.println(RLP.bytesToAscii(b1));
		
		
	}
}

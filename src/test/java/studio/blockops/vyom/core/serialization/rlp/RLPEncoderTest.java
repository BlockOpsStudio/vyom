package studio.blockops.vyom.core.serialization.rlp;

import static org.junit.Assert.assertArrayEquals;

import java.math.BigInteger;

import javax.inject.Inject;

import org.ethereum.util.ByteUtil;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class RLPEncoderTest {
	
	@Inject
	protected RLPEncoder encoder;

	@RunWith(JukitoRunner.class)
	public static class EncodeByteTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(ByteTestData.class, 
						new ByteTestData((byte) 0, "80"),
						new ByteTestData((byte) 120, "78"),
						new ByteTestData((byte) 127, "7F"));
			}
		}

		@Test
		public void encodeByteTest(@All ByteTestData data) {
			encoder.encodeByte(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class ByteTestData extends TestData {
			private final byte input;
			private ByteTestData(byte input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class EncodeShortTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(ShortTestData.class, 
						new ShortTestData((short) 0, "80"),
						new ShortTestData((short) 120, "78"),
						new ShortTestData((short) 127, "7F"),
						
						new ShortTestData((short) 30303, "82 76 5F"),
						new ShortTestData((short) 20202, "82 4E EA"),
						new ShortTestData((short) 40202, "82 9D 0A"));
			}
		}

		@Test
		public void encodeShortTest(@All ShortTestData data) {
			encoder.encodeShort(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class ShortTestData extends TestData {
			private final short input;
			private ShortTestData(short input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class EncodeIntTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(IntTestData.class, 
						new IntTestData(0, "80"),
						new IntTestData(120, "78"),
						new IntTestData(127, "7F"),

						new IntTestData(1024, "82 04 00"),
						new IntTestData(30303, "82 76 5F"),
						new IntTestData(20202, "82 4E EA"),
						new IntTestData(65536, "83 01 00 00"),
						
						new IntTestData(Integer.MIN_VALUE, "84 80 00 00 00"),
						new IntTestData(Integer.MAX_VALUE, "84 7F FF FF FF"),
						
						new IntTestData(0xFFFFFFFF, "84 FF FF FF FF"));
			}
		}

		@Test
		public void encodeIntTest(@All IntTestData data) {
			encoder.encodeInt(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class IntTestData extends TestData {
			private final int input;
			private IntTestData(int input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class EncodeLongTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(LongTestData.class, 
						new LongTestData(0L, "80"),
						new LongTestData(120L, "78"),
						new LongTestData(127L, "7F"),

						new LongTestData(1024L, "82 04 00"),
						new LongTestData(30303L, "82 76 5F"),
						new LongTestData(20202L, "82 4E EA"),
						new LongTestData(65536L, "83 01 00 00"),						
						new LongTestData((long) Integer.MIN_VALUE, "84 80 00 00 00"),
						new LongTestData((long) Integer.MAX_VALUE, "84 7F FF FF FF"),						
						new LongTestData(0xFFFFFFFFL, "84 FF FF FF FF"),
						
						new LongTestData(0xBF05138516EL,      "86 0B F0 51 38 51 6E"),
						new LongTestData(0x3CC1093F212CL,     "86 3C C1 09 3F 21 2C"),
						new LongTestData(0xF7D4BD5CC6CB1L,    "87 0F 7D 4B D5 CC 6C B1"),
						new LongTestData(0x6BD9D719209BF4L,   "87 6B D9 D7 19 20 9B F4"),
						new LongTestData(0x877A6F77F0DDA6BL,  "88 08 77 A6 F7 7F 0D DA 6B"),
						new LongTestData(Long.MIN_VALUE,      "88 80 00 00 00 00 00 00 00"),
						new LongTestData(Long.MAX_VALUE,      "88 7F FF FF FF FF FF FF FF"),
						new LongTestData(0xFFFFFFFFFFFFFFFFL, "88 FF FF FF FF FF FF FF FF"));
			}
		}

		@Test
		public void encodeLongTest(@All LongTestData data) {
			encoder.encodeLong(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class LongTestData extends TestData {
			private final long input;
			private LongTestData(long input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class EncodeBigIntegerTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(BigIntegerTestData.class, 
						BigIntegerTestData.fromDecimal("0", "80"),
						BigIntegerTestData.fromDecimal("120", "78"),
						BigIntegerTestData.fromDecimal("127", "7F"),

						BigIntegerTestData.fromDecimal("1024", "82 04 00"),
						BigIntegerTestData.fromDecimal("30303", "82 76 5F"),
						BigIntegerTestData.fromDecimal("20202", "82 4E EA"),
						BigIntegerTestData.fromDecimal("65536", "83 01 00 00"),	
						BigIntegerTestData.fromDecimal("2147483647", "84 7F FF FF FF"),
						BigIntegerTestData.fromHex("FFFFFFFF", "84 FF FF FF FF"),
						
						BigIntegerTestData.fromHex("BF05138516E",      "86 0B F0 51 38 51 6E"),
						BigIntegerTestData.fromHex("3CC1093F212C",     "86 3C C1 09 3F 21 2C"),
						BigIntegerTestData.fromHex("F7D4BD5CC6CB1",    "87 0F 7D 4B D5 CC 6C B1"),
						BigIntegerTestData.fromHex("6BD9D719209BF4",   "87 6B D9 D7 19 20 9B F4"),
						BigIntegerTestData.fromHex("877A6F77F0DDA6B",  "88 08 77 A6 F7 7F 0D DA 6B"),
						BigIntegerTestData.fromHex("FFFFFFFFFFFFFFFF", "88 FF FF FF FF FF FF FF FF"),
						
						BigIntegerTestData.fromHex("58e14dd5e30ffd5543220f03ee92cbcc7a692a9e",          "94 58e14dd5e30ffd5543220f03ee92cbcc7a692a9e"),
						BigIntegerTestData.fromHex("a07989f9261f4c8a8b46ec5d98697c4a79e877c2650",       "96 0a07989f9261f4c8a8b46ec5d98697c4a79e877c2650"),
						BigIntegerTestData.fromHex("de0db635daed5b017f273e762ee5c4c9f34a5d33e4107a",    "97 de0db635daed5b017f273e762ee5c4c9f34a5d33e4107a"),
						BigIntegerTestData.fromHex("406e6aee1efb660ceb3d2aa6f6ec4eea1c1439fcff6d4a0c1", "99 0406e6aee1efb660ceb3d2aa6f6ec4eea1c1439fcff6d4a0c1"),
						
						BigIntegerTestData.fromHex("6fdd4e7e1d2cba001db91a3c931148611d5cf10b8a294b1f3454c91f2700",          "9E 6fdd4e7e1d2cba001db91a3c931148611d5cf10b8a294b1f3454c91f2700"),
						BigIntegerTestData.fromHex("b344f2dd3d7cf6833bccaa958d6debd96c6e1715ccd7ac7a88601220b90b960",       "A0 0b344f2dd3d7cf6833bccaa958d6debd96c6e1715ccd7ac7a88601220b90b960"),
						BigIntegerTestData.fromHex("3d3b7904ebbeb85884ab9fb89ba7413ebdb5a07ec906250d9c1eca3f77dbe75a60",    "A1 3d3b7904ebbeb85884ab9fb89ba7413ebdb5a07ec906250d9c1eca3f77dbe75a60"),
						BigIntegerTestData.fromHex("7b45018451b7f0f5565f4a066fad176a79819f04a3de7cf49b9a0099e4948d767f5f8", "A3 07b45018451b7f0f5565f4a066fad176a79819f04a3de7cf49b9a0099e4948d767f5f8"));
			}
		}

		@Test
		public void encodeBigIntegerTest(@All BigIntegerTestData data) {
			encoder.encodeBigInteger(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class BigIntegerTestData extends TestData {
			private final BigInteger input;
			private static BigIntegerTestData fromDecimal(String input, String expected) {
				return new BigIntegerTestData(new BigInteger(input), expected);
			}
			private static BigIntegerTestData fromHex(String input, String expected) {
				return new BigIntegerTestData(new BigInteger(input, 16), expected);
			}
			private BigIntegerTestData(BigInteger input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}
	
	private static abstract class TestData {
		protected final byte[] expected;
		private TestData(String expected) {
			expected = expected.replaceAll("\\s","");		// Remove all whitespaces
			this.expected = ByteUtil.hexStringToBytes(expected);
		}
	}

}

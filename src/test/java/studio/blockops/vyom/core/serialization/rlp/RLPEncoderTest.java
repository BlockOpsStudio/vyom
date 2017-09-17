package studio.blockops.vyom.core.serialization.rlp;

import static org.junit.Assert.assertArrayEquals;

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
	
	private static abstract class TestData {
		protected final byte[] expected;
		private TestData(String expected) {
			expected = expected.replaceAll("\\s","");		// Remove all whitespaces
			this.expected = ByteUtil.hexStringToBytes(expected);
		}
	}

}

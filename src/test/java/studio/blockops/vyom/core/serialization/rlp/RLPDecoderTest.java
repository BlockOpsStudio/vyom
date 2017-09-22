package studio.blockops.vyom.core.serialization.rlp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigInteger;

import org.ethereum.util.ByteUtil;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import studio.blockops.vyom.core.serialization.DecoderException;

@RunWith(Enclosed.class)
public class RLPDecoderTest {

	@RunWith(JukitoRunner.class)
	public static class DecodeByteTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(ByteTestData.class, "t1",
						new ByteTestData((byte) 0, "80"),
						new ByteTestData((byte) 120, "78"),
						new ByteTestData((byte) 127, "7F"),
						new ByteTestData((byte) 128, "81 80"),
						new ByteTestData((byte) 143, "81 8F"));

				bindManyNamedInstances(ByteTestData.class, "ex",
						new ByteTestData((byte) 0, ""),
						new ByteTestData((byte) 0, "82 76 5F"),
						new ByteTestData((byte) 0, "86 0B F0 51 38 51 6E"));
			}
		}

		@Test
		public void decodeByteTest(@All("t1") ByteTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			byte actual = decoder.decodeByte();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeByteExceptionTest(@All("ex") ByteTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeByte();
		}

		private static class ByteTestData extends TestData {
			private final byte expected;
			private ByteTestData(byte expected, String input) {
				super(input);
				this.expected = expected;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeShortTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(ShortTestData.class, "t1",
						new ShortTestData((short) 0, "80"),
						new ShortTestData((short) 120, "78"),
						new ShortTestData((short) 127, "7F"),
                        new ShortTestData((short) 128, "81 80"),

						new ShortTestData((short) 30303, "82 76 5F"),
						new ShortTestData((short) 20202, "82 4E EA"),
						new ShortTestData((short) 40202, "82 9D 0A"));

				bindManyNamedInstances(ShortTestData.class, "ex",
						new ShortTestData((short) 0, ""),
						new ShortTestData((short) 0, "83 01 00 00"),
						new ShortTestData((short) 0, "86 0B F0 51 38 51 6E"));
			}
		}

		@Test
		public void decodeShortTest(@All("t1") ShortTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			short actual = decoder.decodeShort();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeShortExceptionTest(@All("ex") ShortTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeShort();
		}

		private static class ShortTestData extends TestData {
			private final short expected;
			private ShortTestData(short expected, String input) {
				super(input);
				this.expected = expected;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeIntTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(IntTestData.class, "t1",
						new IntTestData(0, "80"),
						new IntTestData(120, "78"),
						new IntTestData(127, "7F"),
						new IntTestData(128, "81 80"),

						new IntTestData(1024, "82 04 00"),
						new IntTestData(30303, "82 76 5F"),
						new IntTestData(20202, "82 4E EA"),
						new IntTestData(65536, "83 01 00 00"),

						new IntTestData(Integer.MIN_VALUE, "84 80 00 00 00"),
						new IntTestData(Integer.MAX_VALUE, "84 7F FF FF FF"),

						new IntTestData(0xFFFFFFFF, "84 FF FF FF FF"));

				bindManyNamedInstances(IntTestData.class, "ex",
						new IntTestData(0, ""),
						new IntTestData(0, "85 F0 51 38 51 6E"),
						new IntTestData(0, "86 0B F0 51 38 51 6E"),
						new IntTestData(0, "87 6B D9 D7 19 20 9B F4"));
			}
		}

		@Test
		public void decodeIntTest(@All("t1") IntTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			int actual = decoder.decodeInt();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeIntExceptionTest(@All("ex") IntTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeInt();
		}

		private static class IntTestData extends TestData {
			private final int expected;
			private IntTestData(int expected, String input) {
				super(input);
				this.expected = expected;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeLongTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(LongTestData.class, "t1",
						new LongTestData(0L, "80"),
						new LongTestData(120L, "78"),
						new LongTestData(127L, "7F"),
						new LongTestData(128L, "81 80"),

						new LongTestData(1024L, "82 04 00"),
						new LongTestData(30303L, "82 76 5F"),
						new LongTestData(20202L, "82 4E EA"),
						new LongTestData(65536L, "83 01 00 00"),
						new LongTestData((long) Integer.MAX_VALUE, "84 7F FF FF FF"),
						new LongTestData(0xFFFFFFFFL, "84 FF FF FF FF"),

						new LongTestData(0xBF05138516EL,      "86 0B F0 51 38 51 6E"),
						new LongTestData(0x3CC1093F212CL,     "86 3C C1 09 3F 21 2C"),
						new LongTestData(0xF7D4BD5CC6CB1L,    "87 0F 7D 4B D5 CC 6C B1"),
						new LongTestData(0x6BD9D719209BF4L,   "87 6B D9 D7 19 20 9B F4"),
						new LongTestData(0x877A6F77F0DDA6BL,  "88 08 77 A6 F7 7F 0D DA 6B"),
						new LongTestData(Long.MAX_VALUE,	  "88 7F FF FF FF FF FF FF FF"),

						new LongTestData((long) Integer.MIN_VALUE, "88 FF FF FF FF 80 00 00 00"),
						new LongTestData(Long.MIN_VALUE,		   "88 80 00 00 00 00 00 00 00"),
						new LongTestData(0xFFFFFF8000000000L,	   "88 FF FF FF 80 00 00 00 00"),
						new LongTestData(0xFFFF800000000000L,	   "88 FF FF 80 00 00 00 00 00"),
						new LongTestData(0xFF80000000000000L,	   "88 FF 80 00 00 00 00 00 00"),
						new LongTestData(0xFFFFFFFFFFFFFFFFL,	   "88 FF FF FF FF FF FF FF FF"));

				bindManyNamedInstances(LongTestData.class, "ex",
						new LongTestData(0L, ""),
						new LongTestData(0L, "89 3C C1 09 3F 21 2C 0F 7D 4B"),
						new LongTestData(0L, "9A 0F 7D 4B D5 CC 6C B1 6B D9 19"),
						new LongTestData(0L, "9B 6B D9 D7 19 20 9B F4 08 77 A6 F7"),
						new LongTestData(0L, "9C 08 77 A6 F7 7F 0D DA 6B 08 77 A6 F7 7F"));
			}
		}

		@Test
		public void decodeLongTest(@All("t1") LongTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			long actual = decoder.decodeLong();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeLongExceptionTest(@All("ex") LongTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeLong();
		}

		private static class LongTestData extends TestData {
			private final long expected;
			private LongTestData(long expected, String input) {
				super(input);
				this.expected = expected;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeBigIntegerTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(BigIntegerTestData.class, "t1",
						BigIntegerTestData.fromDecimal("0", "80"),
						BigIntegerTestData.fromDecimal("120", "78"),
						BigIntegerTestData.fromDecimal("127", "7F"),
						BigIntegerTestData.fromDecimal("128", "81 80"),

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

						BigIntegerTestData.fromHex(
								"6fdd4e7e1d2cba001db91a3c931148611d5cf10b8a294b1f3454c91f2700",
								"9E 6fdd4e7e1d2cba001db91a3c931148611d5cf10b8a294b1f3454c91f2700"),
						BigIntegerTestData.fromHex(
								"b344f2dd3d7cf6833bccaa958d6debd96c6e1715ccd7ac7a88601220b90b960",
								"A0 0b344f2dd3d7cf6833bccaa958d6debd96c6e1715ccd7ac7a88601220b90b960"),
						BigIntegerTestData.fromHex(
								"3d3b7904ebbeb85884ab9fb89ba7413ebdb5a07ec906250d9c1eca3f77dbe75a60",
								"A1 3d3b7904ebbeb85884ab9fb89ba7413ebdb5a07ec906250d9c1eca3f77dbe75a60"),
						BigIntegerTestData.fromHex(
								"7b45018451b7f0f5565f4a066fad176a79819f04a3de7cf49b9a0099e4948d767f5f8",
								"A3 07b45018451b7f0f5565f4a066fad176a79819f04a3de7cf49b9a0099e4948d767f5f8"));

				bindManyNamedInstances(BigIntegerTestData.class, "ex",
						BigIntegerTestData.fromHex("0", ""),
						BigIntegerTestData.fromHex("0", "A1 07b45018451b7f"),
						BigIntegerTestData.fromHex("0", "C0 58e14dd5e30ffd5543220f03ee92cbcc7a692a9e"),
						BigIntegerTestData.fromHex("0", "C2 0a07989f9261f4c8a8b46ec5d98697c4a79e877c2650"),
						BigIntegerTestData.fromHex("0", "C4 de0db635daed5b017f273e762ee5c4c9f34a5d33e4107a"),
						BigIntegerTestData.fromHex("0", "C6 0406e6aee1efb660ceb3d2aa6f6ec4eea1c1439fcff6d4a0c1"));
			}
		}

		@Test
		public void decodeBigIntegerTest(@All("t1") BigIntegerTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			BigInteger actual = decoder.decodeBigInteger();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeBigIntegerExceptionTest(@All("ex") BigIntegerTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeBigInteger();
		}

		private static class BigIntegerTestData extends TestData {
			private final BigInteger expected;
			private static BigIntegerTestData fromDecimal(String expected, String input) {
				return new BigIntegerTestData(new BigInteger(expected), input);
			}
			private static BigIntegerTestData fromHex(String expected, String input) {
				return new BigIntegerTestData(new BigInteger(expected, 16), input);
			}
			private BigIntegerTestData(BigInteger expected, String input) {
				super(input);
				this.expected = expected;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeBytesTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(BytesTestData.class, "t1",
						new BytesTestData("4e0117f398a9a9f894a4d49c446e41729ff0cde9bc9",          "96 04e0117f398a9a9f894a4d49c446e41729ff0cde9bc9"),
						new BytesTestData("87205cf58319be7eb1c6fccc78d0ea15e655ddb519ffe9",       "97 87205cf58319be7eb1c6fccc78d0ea15e655ddb519ffe9"),
						new BytesTestData("6b19c2ceb52227ceedd4b2f7730772c3364e55055252b8739",    "99 06b19c2ceb52227ceedd4b2f7730772c3364e55055252b8739"),
						new BytesTestData("63a64bafaebe60847eb13f8be2be0c7747f36359f927d47261b3", "9A 63a64bafaebe60847eb13f8be2be0c7747f36359f927d47261b3"),

						new BytesTestData(
								"4909b7bd1518666441be1e2ccc7e9fa0cc1a588c39b1f9743995a017f5b9c3363863a7619e70d42b203746dda06fc8627f6d252",
								"B4 04909b7bd1518666441be1e2ccc7e9fa0cc1a588c39b1f9743995a017f5b9c3363863a7619e70d42b203746dda06fc8627f6d252"),
						new BytesTestData(
								"35ebe7fc9980fa6245da7a1deb082b4dbb4878d3e0c06c002ada830381bccbe052845cc054f44b31bd1c43ec8901e01bcc3d92a94f",
								"B5 35ebe7fc9980fa6245da7a1deb082b4dbb4878d3e0c06c002ada830381bccbe052845cc054f44b31bd1c43ec8901e01bcc3d92a94f"),
						new BytesTestData(
								"9c94142d333381be8679190841b014dc54e17d079a4435b0b0ebd350148298f4c7d385fd99456c6ae0b189d0c6e79d868d92a4b5479f4",
								"B7 09c94142d333381be8679190841b014dc54e17d079a4435b0b0ebd350148298f4c7d385fd99456c6ae0b189d0c6e79d868d92a4b5479f4"),

						new BytesTestData(
								"575d43b6c88e10b71ec08e8cceb3975031fd18394230ded3a74c505f63de623fe05855dea6e6c897d874fc76797148271f8c39abeadcd35c",
								"B8 38 575d43b6c88e10b71ec08e8cceb3975031fd18394230ded3a74c505f63de623fe05855dea6e6c897d874fc76797148271f8c39abeadcd35c"),

						new BytesTestData(
								"3b9a68e2d491dcaac49afefec93afe2ceadd7d9fa695e2f3b90c5ea41accf199f88fd9b88a677abb7158536268a6582becce0aef396805aaebfa11fbfd5e56b1d730b136f227e898f8b1a938511fb711ac2b6bdf791fa27dfcdf4d1afa07ec572073ce73209e042f1a139733eef2de328711c864591e29e6cd94404770bb3236f7d4e30fdc3ae85857fea7b0330763ee7831bb6ce079f3f0e1aa74dabc8c63a851eec3153c1da2e9e9d49481d72c06117743c4bddbd5b019888b12ebaecfe19120e4e3f97762270781227597361e128414a58097cdcdf75e537b5fead264fe1d8988a6d2b7893bbadce24389bd8ddf58ea452ecdf7c1d23d4c04e484a63929b9f165054ac3d11606f56b5e0569e32203acd1b06559305cc57c66d74f2a49df8f4091b51c4572943f254fbcde8d877",
								"B9 01 2F 03b9a68e2d491dcaac49afefec93afe2ceadd7d9fa695e2f3b90c5ea41accf199f88fd9b88a677abb7158536268a6582becce0aef396805aaebfa11fbfd5e56b1d730b136f227e898f8b1a938511fb711ac2b6bdf791fa27dfcdf4d1afa07ec572073ce73209e042f1a139733eef2de328711c864591e29e6cd94404770bb3236f7d4e30fdc3ae85857fea7b0330763ee7831bb6ce079f3f0e1aa74dabc8c63a851eec3153c1da2e9e9d49481d72c06117743c4bddbd5b019888b12ebaecfe19120e4e3f97762270781227597361e128414a58097cdcdf75e537b5fead264fe1d8988a6d2b7893bbadce24389bd8ddf58ea452ecdf7c1d23d4c04e484a63929b9f165054ac3d11606f56b5e0569e32203acd1b06559305cc57c66d74f2a49df8f4091b51c4572943f254fbcde8d877"),

						/* ethereumJ Tests */
						// - Single bytes
						new BytesTestData("00", "00"),
						new BytesTestData("01", "01"),
						new BytesTestData("78", "78"),
						new BytesTestData("7F", "7F"),

						// - Two bytes
						new BytesTestData("80", "81 80"),

						// - Empty byte array
						new BytesTestData("", "80"),

						new BytesTestData(
								"ce73660a06626c1b3fda7b18ef7ba3ce17b6bf604f9541d3c6c654b7ae88b239407f659c78f419025d785727ed017b6add21952d7e12007373e321dbc31824ba",
								"B8 40 ce73660a06626c1b3fda7b18ef7ba3ce17b6bf604f9541d3c6c654b7ae88b239407f659c78f419025d785727ed017b6add21952d7e12007373e321dbc31824ba"));

				bindManyNamedInstances(BytesTestData.class, "ex",
						new BytesTestData("", ""),
						new BytesTestData("", "97 87205cf58319be7eb1c6fccc78d0ea"),
						new BytesTestData("", "C0 04e0117f398a9a9f894a4d49c446e41729ff0cde9bc9"),
						new BytesTestData("", "C2 87205cf58319be7eb1c6fccc78d0ea15e655ddb519ffe9"),
						new BytesTestData("", "C4 06b19c2ceb52227ceedd4b2f7730772c3364e55055252b8739"),
						new BytesTestData("", "C6 63a64bafaebe60847eb13f8be2be0c7747f36359f927d47261b3"));
			}
		}

		@Test
		public void decodeBytesTest(@All("t1") BytesTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			byte[] actual = decoder.decodeBytes();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeBytesExceptionTest(@All("ex") BytesTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeBytes();
		}

		private static class BytesTestData extends TestData {
			private final byte[] expected;
			private BytesTestData(String expected, String input) {
				super(input);
				expected = expected.replaceAll("\\s","");	  // Remove all whitespaces
				this.expected = ByteUtil.hexStringToBytes(expected);
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeStringTest extends RLPDecoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(StringTestData.class, "t1",
						new StringTestData("dog", new byte[]{(byte) 0x83, 'd','o','g'}),
						new StringTestData(
								"Lorem ipsum dolor sit amet, consectetur adipisicing elit",
								new byte[]{(byte) 0xb8, (byte) 0x38, 'L','o','r','e','m',' ','i','p','s','u','m',' ','d','o','l','o','r',' ','s','i','t',' ','a','m','e','t',',',' ','c','o','n','s','e','c','t','e','t','u','r',' ','a','d','i','p','i','s','i','c','i','n','g',' ','e','l','i','t'}),

						/* ethereumJ Tests */
						new StringTestData(
								"EthereumJ Client",
								"90 45 74 68 65 72 65 75 6D 4A 20 43 6C 69 65 6E 74"),
						new StringTestData(
								"Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++",
								"AD 45 74 68 65 72 65 75 6D 28 2B 2B 29 2F 5A 65 72 6F 47 6F 78 2F 76 30 2E 35 2E 30 2F 6E 63 75 72 73 65 73 2F 4C 69 6E 75 78 2F 67 2B 2B"),
						new StringTestData(
								"Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++",
								"B8 5A 45 74 68 65 72 65 75 6D 28 2B 2B 29 2F 5A 65 72 6F 47 6F 78 2F 76 30 2E 35 2E 30 2F 6E 63 75 72 73 65 73 2F 4C 69 6E 75 78 2F 67 2B 2B 45 74 68 65 72 65 75 6D 28 2B 2B 29 2F 5A 65 72 6F 47 6F 78 2F 76 30 2E 35 2E 30 2F 6E 63 75 72 73 65 73 2F 4C 69 6E 75 78 2F 67 2B 2B"));

				bindManyNamedInstances(StringTestData.class, "ex",
						new StringTestData("", new byte[]{}),
						new StringTestData(
								"Lorem ipsum dolor sit amet, consectetur adipisicing elit",
								new byte[]{(byte) 0xb8, (byte) 0x38, 'L','o','r','e','m',' ','i','p','s','u','m'}),
						new StringTestData(
								"Failed test string",
								new byte[]{(byte) 0xca, (byte) 0x38, 'F','a','i','l','e','d',' ','t','e','s','t',' ','s','t','r','i','n','g'}));
			}
		}

		@Test
		public void decodeStringTest(@All("t1") StringTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);

			String actual = decoder.decodeString();

			assertThat(actual, equalTo(data.expected));
		}

		@Test(expected = DecoderException.class)
		public void decodeStringExceptionTest(@All("ex") StringTestData data) {
			final RLPDecoder decoder = new RLPDecoder(data.input);
			decoder.decodeString();
		}

		private static class StringTestData extends TestData {
			private final String expected;
			private StringTestData(String expected, String input) {
				super(input);
				this.expected = expected;
			}
			private StringTestData(String expected, byte[] input) {
				super(input);
				this.expected = expected;
			}
		}
	}

	private static abstract class TestData {
		protected final byte[] input;
		private TestData(String input) {
			input = input.replaceAll("\\s","");  // Remove all whitespaces
			this.input = ByteUtil.hexStringToBytes(input);
		}
		private TestData(byte[] input) {
			this.input = input;
		}
	}

}

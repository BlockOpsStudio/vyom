package studio.blockops.vyom.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class PublicKeyTest {

    @RunWith(JukitoRunner.class)
    public static class RawPublicKeyTest extends PublicKeyTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                bindManyInstances(TestData.class,
                        new TestData(new byte[] { 0x22, (byte) 0xAB, 0x71 }),
                        new TestData(new byte[] { 0x7A, (byte) 0xAB, 0x5D }));
            }
        }

        @Test
        public void canCreateFromBytes(@All TestData data) {
            final PublicKey key = PublicKey.create(data.bytes);

            assertThat(key.getRaw(), equalTo(data.bytes));
        }

        @Test(expected = NullPointerException.class)
        public void cannotCreateFromNull() {
            PublicKey.create(null);
        }

        private final static class TestData {

            private final byte[] bytes;

            private TestData(byte[] bytes) {
                this.bytes = bytes;
            }
        }
    }

    @RunWith(JukitoRunner.class)
    public static class HexStringPublicKeyTest extends PublicKeyTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                bindManyNamedInstances(TestData.class, "can",
                        new TestData("08e3", new byte[] { 0x08, (byte) 0xe3 }),
                        new TestData("227F", new byte[] { 0x22, 0x7f }),
                        new TestData("7461", new byte[] { 0x74, 0x61 }));
                bindManyNamedInstances(String.class, "cannot", "1H33", "22G75");
            }
        }

        @Test
        public void canCreateFromHexString(@All("can") TestData data) {
            final PublicKey key = PublicKey.createFromHexString(data.value);

            assertThat(key.getRaw(), equalTo(data.expected));
        }

        @Test(expected = CryptoException.class)
        public void cannotCreateFromHexString(@All("cannot") String value) {
            PublicKey.createFromHexString(value);
        }

        @Test(expected = NullPointerException.class)
        public void cannotCreateFromNull() {
            PublicKey.createFromHexString(null);
        }

        private final static class TestData {

            private final String value;

            private final byte[] expected;

            private TestData(String value, byte[] expected) {
                this.value = value;
                this.expected = expected;
            }
        }
    }

    @RunWith(JukitoRunner.class)
    public static class EqualsPublicKeyTest extends PublicKeyTest {

        @Test
        public void equalsOnlyReturnsTrueForEquivalentObjects() {
            final PublicKey key = PublicKey.create(new byte[] { 0x22, (byte) 0xAB, 0x71 });

            assertThat(key, is(notNullValue()));
            assertThat(key, equalTo(PublicKey.createFromHexString("22ab71")));
            assertThat(key, is(not(equalTo(PublicKey.createFromHexString("22ab72")))));
        }
    }

    @RunWith(JukitoRunner.class)
    public static class HashCodePublicKeyTest extends PublicKeyTest {

        @Test
        public void hashCodeOnlyReturnsTrueForEquivalentObjects() {
            final PublicKey key = PublicKey.create(new byte[] { 0x22, (byte) 0xAB, 0x71 });
            final int hashCode = key.hashCode();

            assertThat(hashCode, equalTo(PublicKey.createFromHexString("22ab71").hashCode()));
            assertThat(hashCode, is(not(equalTo(PublicKey.createFromHexString("22ab72").hashCode()))));
        }
    }

    @RunWith(JukitoRunner.class)
    public static class ToStringPublicKeyTest extends PublicKeyTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                bindManyNamedInstances(TestData.class, "hex",
                        new TestData("2275", "2275"),
                        new TestData("227F", "227f"),
                        new TestData("08e3", "08e3"),
                        new TestData("22ab71", "22ab71"));
            }
        }

        @Test
        public void toStringRawPublicKeyTest() {
            final PublicKey key = PublicKey.create(new byte[] { 0x22, (byte) 0xAB, 0x71 });

            assertThat(key.toString(), equalTo("22ab71"));
        }

        @Test
        public void toStringHexStringPublicKeyTest(@All TestData data) {
            final PublicKey key = PublicKey.createFromHexString(data.value);

            assertThat(key.toString(), equalTo(data.expected));
        }

        private final static class TestData {

            private final String value;

            private final String expected;

            private TestData(String value, String expected) {
                this.value = value;
                this.expected = expected;
            }
        }
    }

}

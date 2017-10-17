package studio.blockops.vyom.core.transaction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import studio.blockops.vyom.core.Address;
import studio.blockops.vyom.serialization.rlp.RLPDecoder;

@RunWith(Enclosed.class)
public class AddressTest {

    @RunWith(JukitoRunner.class)
    public static class CreateTest extends AddressTest {

        @Test(expected = NullPointerException.class)
        public void nullValidationTest() {
            Address.create(null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void lengthValidationTest() {
            final byte[] address = new byte[]{(byte) 0xb8, (byte) 0x38};
            Address.create(address);
        }
    }

    @RunWith(JukitoRunner.class)
    public static class CreateFromHexStringTest extends AddressTest {

        @Test(expected = NullPointerException.class)
        public void nullValidationTest() {
            Address.createFromHexString(null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void lengthValidationTest() {
            final String address = "5af14f2e6fe985cd3a";
            Address.createFromHexString(address);
        }
    }

    @RunWith(JukitoRunner.class)
    public static class RLPEncodeDecodeTest extends AddressTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                bindManyInstances(String.class,
                        // NEM Test Data
                        "a10dc8ed0385cd4df858026386083e50096c2d48",
                        "5af14f2e6fe985cd3a34384da4341fe7cd62bd13",
                        "a45477c72194650b71aa11c332a3294c9d88061e",
                        "2ec4637a77faaf2080aa9bc6f1f7f38a1edf1cdc",
                        "cf9068aea565a821214b3f195bace8146dca99c3",
                        "8580d9a0f41cc960eb7c7667cb9efe4ef2541ded",
                        "fba40f86237faad048dd9985e71fe408b5a4a95b",
                        "1bf5fc610130945eead741d970c729cb64c0528b",
                        "0d03e2eb10dc6463073195be0c9a601a322a2074",
                        "44fa11298b1e667bda0085b083fc71a68d576fa4",
                        "d6af97dbe3c22e75d211c0e7fb3a31ed0ee4a969",
                        "fa75b1ae8599a268787fbc687c7d75bef0c53e9e",
                        "cedb976a526b21c4e0d3b02add48e57b7c5b608e",
                        "aeece01ae95bdd1ee38eb7fd28cd3415265f9157",
                        "15b5e1851ab16720abed44055ac5c94160a9add1",
                        "72520c88e402e492aa890e664dad73e90552b57a",
                        "563a1de08b7bfbce33d9a7b13fb4c725c41a8259",
                        "44fa11298b1e667bda0085b083fc71a68d576fa4",
                        "b892d48111bfba9546fbb27696a012fe7d8283a1",
                        "7ee6096ea0d3761a221d2c79b281940b53bfca5f",
                        "1e13c18be66df10da16e4710a6cb9cfa8c5ab106",
                        "2bdc9300b8b73f35a1181caf717cfe7e8e5fa9f6",
                        "23279bc8938b9bc318305b50b0b7aaa0cbfa0b58",

                        // ethereumJ Test Data
                        "cd2a3d9f938e13cd947ec05abc7fe734df8dd826");
            }
        }

        @Test
        public void roundTripRLPEncodeDecodeTest(@All String addressHexString) {
            final Address address = Address.createFromHexString(addressHexString);

            final byte[] encodedAddress = address.getEncoded();

            final Address decodedAddress = Address.decode(new RLPDecoder(encodedAddress));

            assertThat(address, equalTo(decodedAddress));
        }
    }

}

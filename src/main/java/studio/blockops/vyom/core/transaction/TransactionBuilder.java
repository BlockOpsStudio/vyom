package studio.blockops.vyom.core.transaction;

import java.math.BigInteger;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

import studio.blockops.vyom.core.Address;
import studio.blockops.vyom.crypto.CryptoEngine;
import studio.blockops.vyom.crypto.Hashing;
import studio.blockops.vyom.crypto.KeyPair;
import studio.blockops.vyom.crypto.Signature;
import studio.blockops.vyom.crypto.Signer;
import studio.blockops.vyom.serialization.rlp.RLPDecoder;

public class TransactionBuilder {

    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;

    private final CryptoEngine engine;

    private final StepBuilder builder;

    private Transaction transaction;

    @Inject
    private TransactionBuilder(final CryptoEngine engine) {
        this.engine = engine;
        this.builder = new StepBuilder();
    }

    public To create(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final BigInteger value,
            final byte[] data,
            final Integer chainID) {
        Preconditions.checkNotNull(nonce);
        Preconditions.checkNotNull(gasPrice);
        Preconditions.checkNotNull(gasLimit);
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(chainID);

        transaction = new Transaction(nonce);
        transaction.setGasPrice(gasPrice);
        transaction.setGasLimit(gasLimit);
        transaction.setValue(value);
        transaction.setData(data);
        transaction.setChainID(chainID);

        return builder;
    }

    public Build decode(final RLPDecoder decoder) {
        Preconditions.checkNotNull(decoder);

        final RLPDecoder listDecoder = new RLPDecoder(decoder.decodeList());

        final BigInteger nonce = listDecoder.decodeBigInteger();

        transaction = new Transaction(nonce);
        transaction.setGasPrice(listDecoder.decodeBigInteger());
        transaction.setGasLimit(listDecoder.decodeBigInteger());
        transaction.setReceiverAddress(Address.decode(listDecoder));
        transaction.setValue(listDecoder.decodeBigInteger());
        transaction.setData(listDecoder.decodeBytes());

        final int v = listDecoder.decodeInt();
        transaction.setChainID(extractChainIdFromV(v));

        final BigInteger r = listDecoder.decodeBigInteger();
        final BigInteger s = listDecoder.decodeBigInteger();
        final Signature signature = Signature.create(r, s, getRealV(v));
        transaction.setSignature(Optional.of(signature));

        return builder;
    }

    private Integer extractChainIdFromV(int v) {
        Preconditions.checkArgument(v <= Integer.MAX_VALUE, "chainId is limited to 31 bits, longer are not valid for now");

        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return null;
        }

        return (int) ((v - CHAIN_ID_INC) / 2);
    }

    private byte getRealV(int v) {
        Preconditions.checkArgument(v <= Integer.MAX_VALUE, "chainId is limited to 31 bits, longer are not valid for now");

        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return (byte) v;
        }

        byte realV = LOWER_REAL_V;
        int inc = 0;
        if ((int) v % 2 == 0) {
            inc = 1;
        }
        return (byte) (realV + inc);
    }

    public interface To {
        Sign to(final Address receiverAddress);
    }

    public interface Sign {
        Build sign(final KeyPair keyPair);
    }

    public interface Build {
        Transaction build();
    }

    class StepBuilder implements To, Sign, Build {

        @Override
        public Sign to(Address receiverAddress) {
            transaction.setReceiverAddress(receiverAddress);
            return this;
        }

        @Override
        public Build sign(KeyPair keyPair) {
            final Signer signer = engine.createSigner(keyPair);
            final Signature signature = signer.sign(encode());
            transaction.setSignature(Optional.of(signature));
            return this;
        }

        @Override
        public Transaction build() {
            final byte[] hash = Hashing.sha3_256(encode());
            transaction.setHash(hash);
            return transaction;
        }

        private final byte[] encode() {
            transaction.encode();
            return transaction.getEncoded();
        }

    }

}

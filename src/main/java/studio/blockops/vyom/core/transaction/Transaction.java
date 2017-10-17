package studio.blockops.vyom.core.transaction;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

import org.ethereum.util.ByteUtil;

import studio.blockops.vyom.core.Address;
import studio.blockops.vyom.crypto.Signature;
import studio.blockops.vyom.serialization.Encodable;
import studio.blockops.vyom.serialization.Encoder;
import studio.blockops.vyom.serialization.rlp.RLPEncoder;

public class Transaction implements Encodable {

    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;

    private final BigInteger nonce;

    private BigInteger gasPrice;

    private BigInteger gasLimit;

    private BigInteger value;

    private byte[] data;

    private Integer chainID;

    private Address receiverAddress;

    private Optional<Signature> signature = Optional.empty();

    private byte[] hash;

    private Optional<byte[]> encoded = Optional.empty();

    Transaction(final BigInteger nonce) {
        this.nonce = nonce;
    }

    @Override
    public void encode(Encoder encoder) {
        encoder.write(getEncoded());
    }

    @Override
    public byte[] getEncoded() {
        if (!encoded.isPresent()) {
            encode();
        }
        return encoded.get();
    }

    void encode() {
        final RLPEncoder listEncoder = new RLPEncoder();
        listEncoder.encodeBigInteger(nonce);
        listEncoder.encodeBigInteger(gasPrice);
        listEncoder.encodeBigInteger(gasLimit);
        listEncoder.encodeObject(receiverAddress);
        listEncoder.encodeBigInteger(value);
        listEncoder.encodeBytes(data);

        if (signature.isPresent()) {
            int v = signature.get().getV() - LOWER_REAL_V;
            v += (chainID * 2 + CHAIN_ID_INC);
            listEncoder.encodeInt(v);
            listEncoder.encodeBigInteger(signature.get().getR());
            listEncoder.encodeBigInteger(signature.get().getS());
        } else {
            listEncoder.encodeInt(chainID);
            listEncoder.encodeBigInteger(BigInteger.ZERO);
            listEncoder.encodeBigInteger(BigInteger.ZERO);
        }

        final RLPEncoder encoder = new RLPEncoder();
        encoder.encodeList(listEncoder.getEncoded());
        encoded = Optional.of(encoder.getEncoded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                nonce,
                gasPrice,
                gasLimit,
                receiverAddress,
                value,
                data,
                chainID,
                signature);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (!(obj instanceof Transaction)) {
            return false;
        }

        final Transaction other = (Transaction) obj;
        return Objects.equals(this.nonce, other.nonce)
                && Objects.equals(this.gasPrice, other.gasPrice)
                && Objects.equals(this.gasLimit, other.gasLimit)
                && Objects.equals(this.receiverAddress, other.receiverAddress)
                && Objects.equals(this.value, other.value)
                && (Objects.equals(this.data, other.data)
                        || (ByteUtil.isNullOrZeroArray(this.data) && ByteUtil.isNullOrZeroArray(other.data)))
                && Objects.equals(this.chainID, other.chainID)
                && Objects.equals(this.signature, other.signature);
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public BigInteger getValue() {
        return value;
    }

    public byte[] getData() {
        return data;
    }

    public Integer getChainID() {
        return chainID;
    }

    public Address getReceiverAddress() {
        return receiverAddress;
    }

    public Optional<Signature> getSignature() {
        return signature;
    }

    public byte[] getHash() {
        return hash;
    }

    void setEncoded(Optional<byte[]> encoded) {
        this.encoded = encoded;
    }

    void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    void setValue(BigInteger value) {
        this.value = value;
    }

    void setData(byte[] data) {
        this.data = data;
    }

    void setChainID(Integer chainID) {
        this.chainID = chainID;
    }

    void setReceiverAddress(Address receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    void setSignature(Optional<Signature> signature) {
        this.signature = signature;
    }

    void setHash(byte[] hash) {
        this.hash = hash;
    }

}

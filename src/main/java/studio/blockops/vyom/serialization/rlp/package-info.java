/**
 * Recursive Length Prefix (RLP) encoding.
 * <p>
 * The purpose of RLP is to encode arbitrarily nested arrays of binary data, and
 * RLP is the main encoding method used to serialize objects in Ethereum. The
 * only purpose of RLP is to encode structure; encoding specific atomic data
 * types (eg. strings, integers, floats) is left up to higher-order protocols; in
 * Ethereum the standard is that integers are represented in big endian binary
 * form.
 *
 * @see <a href="https://github.com/ethereum/wiki/wiki/%5BEnglish%5D-RLP">Ethereum RLP</a>
 */
package studio.blockops.vyom.serialization.rlp;

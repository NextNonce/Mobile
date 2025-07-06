package com.nextnonce.app.core.domain.chain

/**
 * Represents the type of blockchain.
 *
 * @property EVM Ethereum Virtual Machine compatible chains.
 * @property CAIROVM Cairo Virtual Machine compatible chains.
 * // @property SVM Solana Virtual Machine compatible chains.
 * // More types in the future
 */
enum class ChainType {
    EVM,
    CAIROVM,
    //SVM,
    //...
}
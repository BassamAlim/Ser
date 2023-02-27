package bassamalim.ser.models

data class MyByteKeyPair(
    val public: ByteArray,
    val private: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyByteKeyPair

        if (!public.contentEquals(other.public)) return false
        if (!private.contentEquals(other.private)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = public.contentHashCode()
        result = 31 * result + private.contentHashCode()
        return result
    }

}
package rationals

import java.math.BigInteger

class Rational(val nm: BigInteger, val dm: BigInteger) : Comparable<Rational> {

    operator fun plus(r: Rational): Rational = (nm * r.dm + r.nm * dm).divBy(r.dm * dm)
    operator fun times(r: Rational): Rational = (nm * r.nm).divBy(r.dm * dm)
    operator fun minus(r: Rational): Rational = (nm * r.dm - r.nm * dm).divBy(r.dm * dm)
    operator fun div(r: Rational): Rational = nm.multiply(r.dm).divBy(dm * r.nm)
    operator fun unaryMinus(): Rational = Rational(-nm, dm)

    override fun compareTo(other: Rational): Int = (nm * other.dm).compareTo(other.nm * dm)

    override fun equals(other: Any?): Boolean {
        other as Rational
        return (this === other)
                || (normalize(this).nm.toDouble()
                / (normalize(this).dm.toDouble())
                == (normalize(other).nm.toDouble()
                / (normalize(other).dm.toDouble())))
    }

    override fun toString(): String {
        val r = normalize(this)
        val c1 = (dm == 1.toBigInteger() || nm.rem(dm) == 0.toBigInteger())
        val c2 = (r.dm < 0.toBigInteger() || (r.nm < 0.toBigInteger() && r.dm < 0.toBigInteger()))
        return when {
            c1 -> nm.div(dm).toString()
            c2 -> r.nm.negate().toString() + "/" + r.dm.negate().toString()
            else -> r.nm.toString() + "/" + r.dm.toString()
        }

    }

    override fun hashCode(): Int = 51 * nm.hashCode() + dm.hashCode()

}

infix fun Int.divBy(i: Int): Rational = Rational(this.toBigInteger(), i.toBigInteger())
infix fun BigInteger.divBy(bi: BigInteger): Rational = Rational(this, bi)
infix fun Long.divBy(l: Long): Rational = Rational(this.toBigInteger(), l.toBigInteger())

fun String.toRational(): Rational {
    val n = this.split("/")
    return if (n.size == 1) Rational(n[0].toBigInteger(), 1.toBigInteger())
    else Rational(n[0].toBigInteger(), n[1].toBigInteger())
}

fun normalize(r: Rational): Rational = Rational(
        r.nm.div(highestCommonFactor(r.nm, r.dm).abs()),
        r.dm.div(highestCommonFactor(r.nm, r.dm).abs()))

fun highestCommonFactor(bi1: BigInteger, bi2: BigInteger): BigInteger =
        if (bi2 == 0.toBigInteger()) bi1
        else highestCommonFactor(bi2, bi1 % bi2)


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}
import java.io.File
import kotlin.math.nextUp
import kotlin.random.*

val path = "empresas.txt"
val separator = "\t"
var count = 0
val data = mutableListOf<ColumnData>()
val lines = File(path).readLines()
prepareDataList(lines[0])
lines.forEach {
    count++
    val values = it.split(separator)
    values.forEachIndexed { pos, value ->
        data[pos].onNewValue(value)
    }
}
for (i in 1..(count / 5)) {
    data.forEach {
        print(it.generateRandom() + separator)
    }
    println()
}

fun prepareDataList(row: String) {
    val values = row.split(separator)
    values.forEach {
        it.toIntOrNull()?.let {
            data.add(IntData())
        } ?: run {
            it.toDoubleOrNull()?.let {
                data.add(DoubleData())
            } ?: run {
                data.add(StringData())
            }
        }
    }

}

interface ColumnData {
    fun onNewValue(value: String)
    fun generateRandom(): String
}

class IntData : ColumnData {

    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE

    override fun generateRandom() = Random.nextInt(min, max + 1).toString()

    override fun onNewValue(value: String) {
        value.toIntOrNull()?.let {
            min = minOf(it, min)
            max = maxOf(it, max)
        }
    }
}

class DoubleData : ColumnData {

    var min = Double.MAX_VALUE
    var max = Double.MIN_VALUE

    override fun generateRandom() = Random.nextDouble(min, max.nextUp()).toString()

    override fun onNewValue(value: String) {
        value.toDoubleOrNull()?.let {
            min = minOf(it, min)
            max = maxOf(it, max)
        }
    }
}

class StringData : ColumnData {

    val values = mutableListOf<String>()

    override fun generateRandom() = values.random()

    override fun onNewValue(value: String) {
        if (!values.contains(value)) values.add(value)
    }
}
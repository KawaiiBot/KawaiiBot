package me.aurieh.ichigo.extensions

fun String.splitOnFirst(delimiter: Char): Pair<String, String> {
    val first = StringBuilder()
    val second = StringBuilder()
    var isSecond = false

    for (char in toCharArray()) {
        if (!isSecond && char == delimiter) {
            isSecond = true
            continue
        }
        if (isSecond) {
            second.append(char)
        } else {
            first.append(char)
        }
    }

    return first.toString() to second.toString()
}
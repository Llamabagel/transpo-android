import java.io.BufferedReader
import java.io.InputStreamReader

/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

fun String.execute(): Process = Runtime.getRuntime().exec(this)

val Process.text: String get() = BufferedReader(InputStreamReader(inputStream)).readLine()

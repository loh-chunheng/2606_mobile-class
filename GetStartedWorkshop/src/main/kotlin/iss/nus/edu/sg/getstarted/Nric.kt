package iss.nus.edu.sg.iss.nus.edu.sg.getstarted

fun main() {
    println(isValid("S1234567T"))
    println(isValid("23456781R"))
    println(isValid("T345678Z"))
    println(isValid("Ssodmr81R"))
}

fun isValid(str: String): Boolean {
    if (str.length != 9) return false

    val letters = "STFG"

    if (!letters.contains(str[0])) return false

    if (str.substring(1, 8).toIntOrNull() == null) return false

    if (!str[8].isLetter()) return false

    return true
}
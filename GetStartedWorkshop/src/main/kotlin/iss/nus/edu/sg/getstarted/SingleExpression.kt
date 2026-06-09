package iss.nus.edu.sg.iss.nus.edu.sg.getstarted

fun main() {
    fun max(number1: Int, number2: Int): Int = if (number1 > number2) number1 else number2

    println(max(3,5))
    println(max(9, 9))
    println(max(7,6))
}
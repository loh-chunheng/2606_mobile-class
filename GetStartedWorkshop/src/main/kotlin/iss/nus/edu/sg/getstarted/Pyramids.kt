package iss.nus.edu.sg.iss.nus.edu.sg.getstarted

fun main() {
    println("Enter layer of pyramid (integer):")
    val layer = readln().toInt()

    for (i in 1..layer) {
        for (j in 1..i) {
            print("$j ")
        }
        println()
    }
}
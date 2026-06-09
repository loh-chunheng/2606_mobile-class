package iss.nus.edu.sg.iss.nus.edu.sg.getstarted

fun main() {
    println("Enter length of side 1 (integer):")
    val a = readln().toInt()

    println("Enter length of side 2 (integer):")
    val b = readln().toInt()

    println("Enter length of side 3 (integer):")
    val c = readln().toInt()

    if (a + b <= c || a + c <= b || b + c <= a) {
        println("This is not a triangle.")
    } else if (a == b && a == c) {
        println("The triangle is Equilateral.")
    } else if (a != b && a != c && b != c) {
        println("The triangle is Scalene")
    } else {
        println("The triangle is Isosceles")
    }

}
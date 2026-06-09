package iss.nus.edu.sg.iss.nus.edu.sg.getstarted

fun main() {
    val arr = arrayOf(1,2,2,5,3,4,5,5,2)
    max(arr)

}

fun max(arr: Array<Int>) {
    if (arr.isEmpty()) println("the array is empty")

    var count = 0;
    var max = arr[0]

    for (num in arr) {
        if (num > max) {
            max = num
            count = 1
        } else if (num == max) {
            count ++
        }
    }

    println("$max ($count elements)")
}
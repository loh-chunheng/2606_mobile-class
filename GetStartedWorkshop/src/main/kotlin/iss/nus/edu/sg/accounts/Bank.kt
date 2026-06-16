package iss.nus.edu.sg.iss.nus.edu.sg.accounts

fun main() {
    val myAccount1 = Account("A12345", 500.0)
    myAccount1.deposit(100.0)
    println("Account1: ${myAccount1.number}, balance: ${myAccount1.balance}")

    val myAccount2 = Account()
    myAccount1.deposit(100.0)
    println("Account2: ${myAccount2.number}, balance: ${myAccount2.balance}")

}

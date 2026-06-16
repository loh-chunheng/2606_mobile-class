package iss.nus.edu.sg.iss.nus.edu.sg.accounts

class Account(
    val number: String,
    var balance: Double
) {
    constructor() : this("N11111", 0.0) {}

    fun deposit(amount: Double) {
        this.balance += amount
    }
}
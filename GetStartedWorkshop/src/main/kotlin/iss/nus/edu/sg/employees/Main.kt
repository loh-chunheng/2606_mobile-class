package iss.nus.edu.sg.iss.nus.edu.sg.employees

fun main() {
    val ce = CommissionEmployee(
        name = "Alex Ang",
        identityNumber = "A111111",
        grossSales = 20000.0,
        commissionRate = 0.05
    )
    println(ce)

    val bpce = BasePlusCommissionEmployee(
        name = "John Tee",
        identityNumber = "B222222",
        grossSales = 20000.0,
        commissionRate = 0.025,
        baseSalary = 500.0
    )
    println(bpce)
}
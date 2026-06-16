package iss.nus.edu.sg.iss.nus.edu.sg.employees

class BasePlusCommissionEmployee(
    name: String,
    identityNumber: String,
    grossSales: Double,
    commissionRate: Double,
    val baseSalary: Double
) : CommissionEmployee(name, identityNumber, grossSales, commissionRate) {

    override fun earnings(): Double {
        return super.earnings() + baseSalary
    }
}
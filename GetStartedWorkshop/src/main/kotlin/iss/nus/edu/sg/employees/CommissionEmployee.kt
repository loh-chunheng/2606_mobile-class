package iss.nus.edu.sg.iss.nus.edu.sg.employees

open class CommissionEmployee(
    val name: String,
    val identityNumber: String,
    val grossSales: Double,
    val commissionRate: Double
) {
    open fun earnings(): Double {
        return commissionRate * grossSales
    }

    override fun toString(): String {
        return "${this::class.simpleName}: $name, identify: $identityNumber, gross sales: $grossSales, " +
                "commission rate: $commissionRate, earnings: ${earnings()}"
    }
}
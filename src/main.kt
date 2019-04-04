// TODO: deadlock?


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/1.txt")
    val pheno = Phenotype(p)
    pheno.plot()
}
// TODO: deadlock?
// NOTE: more than 50 jobs crashes because of colourlist in Phenotype.plot()


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/1.txt")
    val pheno = Phenotype(p)
    pheno.fromSequence(mutableListOf(0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5))
    println(pheno.getFitness())
    pheno.plot()
}
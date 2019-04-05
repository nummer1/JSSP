// NOTE: more than 50 jobs crashes because of colourlist in Phenotype.plot()
// TODO: use random key representation instead? works for both PSO and ABC


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/1.txt")
    val pheno = Phenotype(p)

    val geno = Genotype(p)
    val geno2 = Genotype(p)
    geno.randomInitialisation()
    geno2.swapInitialisation(geno)
    println(geno.permutationList)
    println(geno2.permutationList)

    pheno.fromSequence(geno.permutationList)
    println(pheno.getFitness())
    pheno.plot()
}
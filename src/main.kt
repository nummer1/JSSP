// NOTE: more than 50 jobs crashes because of colourlist in Phenotype.plot()
// TODO: use random key representation instead? works for both PSO and ABC


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/1.txt")

    val abc = ABC(p, 100, 20, 10)
    val geno = abc.run()

    println(geno.listRep)
    println(geno.sequence)
    geno.pheno.plot()
}
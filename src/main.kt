// NOTE: more than 50 jobs crashes because of colourlist in Phenotype.plot()


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/5.txt")

    val abc = ABC(p, 1000, 200, 50)
    val geno = abc.run()

    println(geno.listRep)
    println(geno.sequence)
    println(geno.cost)
    geno.pheno.plot()
}
// NOTE: more than 50 jobs crashes because of colourlist in Phenotype.plot()


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/6.txt")

    val abc = ABC(p, 1000, 200, 50)
    val pso = PSO(p, 4000, 200, 0.1)
    val abcGeno = abc.run()
    val psoGeno = pso.run()

    println("abc rep: ${abcGeno.listRep}")
    println("pso rep: ${psoGeno.listRep}")
    println("abc min_cost: ${abcGeno.cost}")
    println("pso min_cost: ${psoGeno.cost}")

    abcGeno.pheno.plot("abcChart")
    psoGeno.pheno.plot("psoChart")

//    println("schedule size: ${geno.pheno.schedule.size}/ correct size: ${p.numberJobs}")
//    for (s in geno.pheno.schedule) {
//        println("    ${s.size}/ ${p.numberMachines}")
//    }
}
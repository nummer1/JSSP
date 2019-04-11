// NOTE: more than 50 jobs crashes because of colourlist in Phenotype.plot()


fun main(args: Array<String>) {
    val p = Problem()
    p.read("Test Data/6.txt")

    // val abc = ABC(p, 1000, 200, 50)
    val pso = PSO(p, 4000, 200, 0.1)
    val geno = pso.run()

    println("sequence: ${geno.listRep}")
    println("cost: ${geno.cost}")

    if (false) {
        println("schedule size: ${geno.pheno.schedule.size}/ correct size: ${p.numberJobs}")
        for (s in geno.pheno.schedule) {
            println("    ${s.size}/ ${p.numberMachines}")
        }
    }

    geno.pheno.plot()
}
import kotlin.random.Random


class ABC(val problem: Problem, val iterations: Int, val populationSize: Int, val limit: Int) {

    private val employeeBees: MutableList<PermutationList>
    private val onlookerBees: MutableList<PermutationList>
    private val cyclesSinceImproved: MutableList<Int>
    private var best: PermutationList

    init {
        employeeBees = mutableListOf()
        onlookerBees = mutableListOf()
        cyclesSinceImproved = mutableListOf()
        best = PermutationList(problem)
    }

    private fun printInfo() {
        println("    abc onlooker average: ${onlookerBees.sumBy { it.cost }/onlookerBees.size}")
        println("    abc employee average: ${employeeBees.sumBy { it.cost }/employeeBees.size}")
        println("    abc min_cost: ${best.cost}")
    }

    fun run(printI: Boolean=false): PermutationList {
        // initialise population
        for (i in 0.until(populationSize)) {
            // initialise employeeBees
            val pop = PermutationList(problem)
            pop.randomInitialisation()
            employeeBees.add(pop)
            cyclesSinceImproved.add(0)
            // initialise employee bees
            onlookerBees.add(pop)
        }

        for (k in 0.until(iterations)) {
            // employeeBees checks for better neighbours
            for ((i, pop) in employeeBees.withIndex()) {
                val randPop = employeeBees[(i + Random.nextInt(1, populationSize)) % populationSize]
                val newPop = PermutationList(problem)
                newPop.neighbourInitialisation(randPop)
                if (newPop.cost < pop.cost) {
                    employeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
            }

            // onlookerBees checks for better neighbour in promising regions
            var totalFitness = 0.0
            val cumulativeFitness = MutableList<Double>(populationSize) { totalFitness += employeeBees[it].fitness; totalFitness }
            for (i in onlookerBees.indices) {
                val rand = Random.nextDouble(0.0, totalFitness)
                var randPop = employeeBees[i]
                for ((j, cf) in cumulativeFitness.withIndex()) {
                    if (rand < cf) {
                        randPop = employeeBees[j]
                        break
                    }
                }

                val newPop = PermutationList(problem)
                newPop.neighbourInitialisation(randPop)
                onlookerBees[i] = newPop
                if (newPop.cost < employeeBees[i].cost) {
                    employeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
            }

            // abandon sites and remember best site
            for (i in cyclesSinceImproved.indices) {
                cyclesSinceImproved[i] += 1
                if (cyclesSinceImproved[i] >= limit) {
                    val newPop = PermutationList(problem)
                    newPop.randomInitialisation()
                    employeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
                if (employeeBees[i].cost < best.cost) {
                    best = PermutationList(problem)
                    best.copyInitialisation(employeeBees[i])
                }
            }

            if (printI && k%100 == 0) {
                println("abc iterations $k:")
                printInfo()
            }
        }

        printInfo()
        return best
    }
}
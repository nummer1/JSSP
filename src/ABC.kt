import kotlin.random.Random


class ABC(val problem: Problem, val iterations: Int, val populationSize: Int, val limit: Int) {

    private val emplyoeeBees: MutableList<PermutationList>
    private val onlookerBees: MutableList<PermutationList>
    private val cyclesSinceImproved: MutableList<Int>

    init {
        emplyoeeBees = mutableListOf()
        onlookerBees = mutableListOf()
        cyclesSinceImproved = mutableListOf()
    }

    fun run(): PermutationList {
        // initialise population
        for (i in 0.until(populationSize)) {
            // initialise employeeBees
            val pop = PermutationList(problem)
            pop.randomInitialisation()
            emplyoeeBees.add(pop)
            cyclesSinceImproved.add(0)
            // initialise employee bees
            onlookerBees.add(pop)
        }

        var bestSolution = emplyoeeBees[0]
        for (k in 0.until(iterations)) {
            // employeeBees checks for better neighbours
            for ((i, pop) in emplyoeeBees.withIndex()) {
                val randPop = emplyoeeBees[(i + Random.nextInt(1, populationSize)) % populationSize]
                val newPop = PermutationList(problem)
                newPop.neighbourInitialisation(randPop)
                if (newPop.cost < pop.cost) {
                    emplyoeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
            }

            // onlookerBees checks for better neighbour in promising regions
            var totalFitness = 0.0
            val cumulativeFitness = MutableList<Double>(populationSize) { totalFitness += emplyoeeBees[it].fitness; totalFitness }
            for (i in onlookerBees.indices) {
                val rand = Random.nextDouble(0.0, totalFitness)
                var randPop = emplyoeeBees[i]
                for ((j, cf) in cumulativeFitness.withIndex()) {
                    if (rand < cf) {
                        randPop = emplyoeeBees[j]
                        break
                    }
                }

                val newPop = PermutationList(problem)
                newPop.neighbourInitialisation(randPop)
                if (newPop.cost < emplyoeeBees[i].cost) {
                    emplyoeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
            }

            // abandon sites and remember best site
            for (i in cyclesSinceImproved.indices) {
                cyclesSinceImproved[i] += 1
                if (cyclesSinceImproved[i] >= limit) {
                    val newPop = PermutationList(problem)
                    newPop.randomInitialisation()
                    emplyoeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
                if (emplyoeeBees[i].cost < bestSolution.cost) {
                    bestSolution = emplyoeeBees[i]
                }
            }
        }
        return bestSolution
    }
}
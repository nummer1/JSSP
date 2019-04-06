import kotlin.random.Random


class ABC(val problem: Problem, val iterations: Int, val populationSize: Int, val limit: Int) {

    val emplyoeeBees: MutableList<RandomKeyList>
    val onlookerBees: MutableList<RandomKeyList>
    val cyclesSinceImproved: MutableList<Int>

    init {
        emplyoeeBees = mutableListOf()
        onlookerBees = mutableListOf()
        cyclesSinceImproved = mutableListOf()
    }

    fun run(): RandomKeyList {
        // initialise population
        // while not stop
        //   employee bees find one neighbour and select greedy
        //   onlookers bees find one neighbour and select greedy
        //   abandon sites that has not improved in $limit cycles
        //   send scouts out
        // neighbour = υmi = xmi + ϕmi(xmi − xki)

        // initialise population
        for (i in 0.until(populationSize)) {
            // initialise employeeBees
            val pop = RandomKeyList(problem)
            pop.randomInitialisation()
            emplyoeeBees.add(pop)
            cyclesSinceImproved.add(0)
            // initialise employee bees
            // TODO: check pop being in two lists does not cause trouble
            onlookerBees.add(pop)
        }

        var bestSolution = emplyoeeBees[0]
        for (k in 0.until(iterations)) {
            // employeeBees check for better neighbours
            for ((i, pop) in emplyoeeBees.withIndex()) {
                val randPop = emplyoeeBees[(i + Random.nextInt(1, populationSize)) % populationSize]
                val newPop = RandomKeyList(problem)
                newPop.neighbourInitialisation(randPop)
                if (newPop.fitness > pop.fitness) {
                    emplyoeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
            }

            // onlookerBees check for better neighbour in promising regions
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

                val newPop = RandomKeyList(problem)
                newPop.neighbourInitialisation(randPop)
                if (newPop.fitness > emplyoeeBees[i].fitness) {
                    emplyoeeBees[i] = newPop
                    cyclesSinceImproved[i] = 0
                }
            }

            for (i in cyclesSinceImproved.indices) {
                cyclesSinceImproved[i] += 1
                if (cyclesSinceImproved[i] >= limit) {
                    val newPop = RandomKeyList(problem)
                    newPop.randomInitialisation()
                    emplyoeeBees[i] = newPop
                }
                if (emplyoeeBees[i].fitness > bestSolution.fitness) {
                    bestSolution = emplyoeeBees[i]
                }
            }
        }
        return bestSolution
    }
}
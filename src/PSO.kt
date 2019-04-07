


class PSO(val problem: Problem, val iterations: Int, val populationSize: Int) {

    private val particles: MutableList<RandomKeyList>

    init {
        particles = mutableListOf()
    }

    fun run(): RandomKeyList {
        // initialise population
        for (i in 0.until(populationSize)) {
            // initialise employeeBees
            val pop = RandomKeyList(problem)
            pop.randomInitialisation()
            particles.add(pop)
        }

        val bestSolution = particles[0]
        for (k in 0.until(iterations)) {

        }
        return bestSolution
    }
}
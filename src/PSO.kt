


class PSO(val problem: Problem, val iterations: Int, val populationSize: Int, val nSize: Int) {

    private val particles: MutableList<RandomKeyList>
    private val neighbourHood: MutableList<Int>

    init {
        particles = mutableListOf()
        neighbourHood = mutableListOf()
    }

    fun run(): RandomKeyList {
        // initialise population
        for (i in 0.until(populationSize)) {
            // initialise employeeBees
            val pop = RandomKeyList(problem)
            pop.randomInitialisation()
            particles.add(pop)

            if (neighbourHood.getOrElse(i%nSize) { -1 } == -1 ) {
                neighbourHood.add(mutableListOf())
            } else {
                neighbourHood[i % nSize].add(i)
            }
        }

        val bestSolution = particles[0]
        for (k in 0.until(iterations)) {
            for (p in particles) {
                p.updatePosition()
            }
        }
        return bestSolution
    }
}
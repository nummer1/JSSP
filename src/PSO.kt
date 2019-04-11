


class PSO(val problem: Problem, val iterations: Int, val populationSize: Int, val maxV: Double) {

    private val particles: MutableList<RandomKeyList>
    private var gBest: RandomKeyList

    init {
        particles = mutableListOf()
        gBest = RandomKeyList(problem, maxV)
    }

    fun run(): RandomKeyList {
        // initialise population
        for (i in 0.until(populationSize)) {
            // initialise employeeBees
            val pop = RandomKeyList(problem, maxV)
            pop.randomInitialisation()
            particles.add(pop)
        }

        for (k in 0.until(iterations)) {

            val best = particles.minBy { it.cost }!!
            if (best.cost < gBest.cost) { gBest = best }

            for (p in particles) {
                p.updatePosition(best.listRep)
            }
        }
        println("pso average: ${particles.sumBy { it.cost }/particles.size}")
        return particles.minBy { it.cost }!!
    }
}
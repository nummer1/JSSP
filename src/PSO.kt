import java.util.*


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

        var best = particles.minBy { it.cost }!!
        if (best.cost < gBest.cost) {
            gBest.copyInitialisation(best)
        }

        for (k in 0.until(iterations)) {
            for (p in particles) {
                p.updatePosition(best.listRep)
            }

            best = particles.minBy { it.cost }!!
            if (best.cost < gBest.cost) {
                gBest = RandomKeyList(problem, maxV)
                gBest.copyInitialisation(best)
            }
        }

        println("pso average: ${particles.sumBy { it.cost }/particles.size}")
        return gBest
    }
}
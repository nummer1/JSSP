import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


class RandomKeyList(val problem: Problem, val maxV: Double) {

    val listRep: MutableList<Double>
    val velocity: MutableList<Double>
    val pBest: MutableList<Double>
    val pheno: Phenotype
    var cost: Int
    var fitness: Double

    init {
        listRep = mutableListOf()
        velocity = mutableListOf()
        pBest = mutableListOf()
        pheno = Phenotype(problem)
        cost = Int.MAX_VALUE
        fitness = 0.0
    }

    private fun checkInitialisation() {
        // prints error if initialisation already happened
        if (listRep.isNotEmpty()) {
            println("Error in RandomKeyList: listRep already initialised")
        }
    }

    private fun createPheno() {
        // create phenotype and calculate cost
        if (listRep.isEmpty()) {
            println("Error in RandomKeyList.createPheno: listRep is not initialised")
        }
        pheno.fromSequenceSerial(toSequenceList1())
        cost = pheno.getCost()
        fitness = 1.0/cost
    }

    fun randomInitialisation() {
        // randomly initialize the keyList
        checkInitialisation()
        for (job in problem.jobs) {
            listRep.addAll(MutableList<Double>(job.size) { Random.nextDouble(-1.0, 1.0) })
            velocity.addAll(MutableList<Double>(job.size) { Random.nextDouble(-maxV, maxV) })
        }
        pBest.addAll(listRep)
        createPheno()
    }

    fun copyInitialisation(randKey: RandomKeyList) {
        checkInitialisation()
        listRep.addAll(randKey.listRep)
        createPheno()
    }

    fun neighbourInitialisation(neighbour: RandomKeyList) {
        // initialise from neighbour as used in artificial bee colony
        checkInitialisation()
        listRep.addAll(neighbour.listRep)
        val randDim = Random.nextInt(0, neighbour.listRep.size)
        listRep[randDim] += Random.nextDouble(-maxV, maxV) * (listRep[randDim] - neighbour.listRep[randDim])
        createPheno()
    }

    fun updatePosition(gBest: MutableList<Double>) {
        // update position with velocity as used in PSO
        for (i in velocity.indices) {
            val v = 1.0 * velocity[i] + (2.0 * Random.nextDouble(0.0, 1.0) * (pBest[i] - listRep[i]) + 2.0 * Random.nextDouble(0.0, 1.0) * (gBest[i] - listRep[i]))
            velocity[i] = if (v < 0) max(v, -maxV) else min(v, maxV)
//            val p = listRep[i] + velocity[i]
//            listRep[i] = if (p < 0) max(p, -maxP) else min(p, maxP)
            listRep[i] += velocity[i]
        }

        val prevCost = cost
        createPheno()
        if (cost < prevCost) {
            pBest.clear()
            pBest.addAll(listRep)
        }
    }

    private fun toSequenceList1(): MutableList<Int> {
        // next job is lowest of all, then run first possible subjob of job
        val indexList = mutableListOf<Pair<Int, Double>>()
        var current = 0
        for ((i, job) in problem.jobs.withIndex()) {
            for (j in current.until(current + job.size)) {
                indexList.add(Pair(i, listRep[j]))
            }
            current += job.size
        }
        indexList.sortBy { it.second }

        val sequence = mutableListOf<Int>()
        for (p in indexList) {
            sequence.add(p.first)
        }
        return sequence
    }

    private fun toSequenceList2(): MutableList<Int> {
        // next subjob is lowest of possible subjobs
        println("Warning: RandomKeyList.toSequenceList2 should not be used")

        val subLists = mutableListOf<MutableList<Double>>()
        var current = 0
        for (job in problem.jobs) {
            subLists.add(listRep.subList(current, current + job.size))
            current += job.size
        }

        // TODO: throws CME
        val sequence = mutableListOf<Int>()
        while (true) {
            val sList = subLists.minBy { it[0] }!!
            val sIndex = subLists.indexOf(sList)
            if (sList[0] == Double.MAX_VALUE) {
                break
            }

            sequence.add(sIndex)
            sList.removeAt(0)
            if (sList.isEmpty()) {
                sList.add(Double.MAX_VALUE)
            }
        }
        return sequence
    }
}


class PermutationList(val problem: Problem) {

    val listRep: MutableList<Int>
    val pheno: Phenotype
    var cost: Int
    var fitness: Double

    init {
        listRep = mutableListOf()
        pheno = Phenotype(problem)
        cost = Int.MAX_VALUE
        fitness = 0.0
    }

    private fun checkInitialisation() {
        // prints error if initialisation already happened
        if (listRep.isNotEmpty()) {
            println("Error in RandomKeyList: listRep already initialised")
        }
    }

    private fun createPheno() {
        // create phenotype and calculate cost
        if (listRep.isEmpty()) {
            println("Error in RandomKeyList.createPheno: listRep is not initialised")
        }
        pheno.fromSequenceSerial(listRep)
        cost = pheno.getCost()
        fitness = 1.0/cost
    }

    fun randomInitialisation() {
        // randomly initialise the listRep
        checkInitialisation()
        for ((i, job) in problem.jobs.withIndex()) {
            listRep.addAll(MutableList<Int>(job.size) { i })
        }
        listRep.shuffle()
        createPheno()
    }

    fun neighbourInitialisation(neighbour: PermutationList) {
        // initialise from neighbour as used in artificial bee colony
        checkInitialisation()
        listRep.addAll(neighbour.listRep)
        val rand = Random.nextDouble(0.0, 1.0)
        when {
            rand < 0.25 -> swap()
            rand < 0.5 -> insert()
            rand < 0.75 -> {swap(); swap()}
            else -> {insert(); insert()}
        }
        createPheno()
    }

    fun copyInitialisation(permList: PermutationList) {
        checkInitialisation()
        listRep.addAll(permList.listRep)
        createPheno()
    }

    private fun swap() {
        // swap two random items
        val rand1 = Random.nextInt(0, listRep.size)
        val rand2 = (rand1 + Random.nextInt(1, listRep.size)) % listRep.size

        if (rand1 == rand2) {
            println("Error in Genotype.swapInitialisation: rand1 == rand2")
        }
        if (rand2 >= listRep.size) {
            println("Error in Genotype.swapInitialisation: rand2 too large")
        }

        listRep[rand1] = listRep[rand2].also { listRep[rand2] = listRep[rand1] }
    }

    private fun insert() {
        // TODO: make not insert at same place
        // move an item to another index
        var rand = Random.nextInt(0, listRep.size)
        val insert = listRep[rand]
        listRep.removeAt(rand)
        rand = Random.nextInt(0, listRep.size)
        listRep.add(rand, insert)
    }
}
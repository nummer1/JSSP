import kotlin.random.Random


class RandomKeyList(val problem: Problem) {

    val listRep: MutableList<Double>
    val sequence: MutableList<Int>
    val pheno: Phenotype
    var cost: Int
    var fitness: Double

    init {
        listRep = mutableListOf()
        sequence = mutableListOf()
        pheno = Phenotype(problem)
        cost = Int.MAX_VALUE
        fitness = 0.0
    }

    private fun createPheno() {
        // create phenotype and calculate cost
        if (listRep.isEmpty()) {
            println("Error in RandomKeyList.createPheno: listRep is not initialised")
        }
        toSequenceList1()
        pheno.fromSequenceSerial(sequence)
        cost = pheno.getCost()
        fitness = 1.0/cost
    }

    private fun checkInitialisation() {
        // prints error if initialisation already happened
        if (listRep.isNotEmpty()) {
            println("Error in RandomKeyList: listRep already initialised")
        }
    }

    fun randomInitialisation() {
        // randomly initialize the keyList
        checkInitialisation()
        for (job in problem.jobs) {
            listRep.addAll(MutableList<Double>(job.size) { Random.nextDouble(-1.0, 1.0) })
        }
        listRep.shuffle()
        createPheno()
    }

    fun neighbourInitialisation(neighbour: RandomKeyList) {
        // initialise from neighbour as used in artificial bee colony
        checkInitialisation()
        listRep.addAll(neighbour.listRep)
        val randDim = Random.nextInt(0, neighbour.listRep.size)
        listRep[randDim] += Random.nextDouble(-1.0, 1.0) * (listRep[randDim] - neighbour.listRep[randDim])
        createPheno()
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

    init {
        listRep = mutableListOf()
    }

    fun randomInitialisation() {
        // randomly initialise the listRep
        for ((i, job) in problem.jobs.withIndex()) {
            listRep.addAll(MutableList<Int>(job.size) { i })
        }
        listRep.shuffle()
    }

    fun swapInitialisation(gene: PermutationList) {
        // swap two random items
        listRep.addAll(gene.listRep)
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

    fun insertInitialisation(gene: PermutationList) {
        // TODO: make not insert at same place
        // move an item to another index
        listRep.addAll(gene.listRep)
        var rand = Random.nextInt(0, listRep.size)
        val insert = listRep[rand]
        listRep.removeAt(rand)
        rand = Random.nextInt(0, listRep.size)
        listRep.add(rand, insert)
    }
}
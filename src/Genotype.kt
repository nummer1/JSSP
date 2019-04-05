import kotlin.random.Random


class Genotype(val problem: Problem) {
    // TODO: if this is only for bees alorithm - rename

    val permutationList: MutableList<Int>

    init {
        permutationList = mutableListOf()
    }

    fun randomInitialisation() {
        // randomly initialise the permutationlist
        for ((i, job) in problem.jobs.withIndex()) {
            permutationList.addAll(MutableList<Int>(job.size) { i })
        }
        permutationList.shuffle()
    }

    fun swapInitialisation(gene: Genotype) {
        // swap two random items
        permutationList.addAll(gene.permutationList)
        val rand1 = Random.nextInt(0, permutationList.size)
        val rand2 = (rand1 + Random.nextInt(1, permutationList.size)) % permutationList.size

        if (rand1 == rand2) {
            println("Error in Genotype.swapInitialisation: rand1 == rand2")
        }
        if (rand2 >= permutationList.size) {
            println("Error in Genotype.swapInitialisation: rand2 too large")
        }

        permutationList[rand1] = permutationList[rand2].also { permutationList[rand2] = permutationList[rand1] }
    }

    fun insertInitialisation(gene: Genotype) {
        // TODO: make not insert at same place
        // move an item to another index
        permutationList.addAll(gene.permutationList)
        var rand = Random.nextInt(0, permutationList.size)
        val insert = permutationList[rand]
        permutationList.removeAt(rand)
        rand = Random.nextInt(0, permutationList.size)
        permutationList.add(rand, insert)
    }
}
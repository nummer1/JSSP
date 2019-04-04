import java.io.File


class Problem {

    var numberJobs: Int = 0
    var numberMachines: Int = 0
    val jobs: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()

    init {

    }

    fun read(filename: String) {
        val fileList: MutableList<String> = mutableListOf()
        File(filename).useLines { lines -> fileList.addAll(lines) }
        val list: List<List<String>> =
            fileList.map { x -> x.split(" ", "\t").filter { y -> y != ("") } }.filter { z -> z.isNotEmpty() }

        numberJobs = list[0][0].toInt()
        numberMachines = list[0][1].toInt()

        for (i in 1.until(list.size)) {
            val jobList: MutableList<Pair<Int, Int>> = mutableListOf()
            for (j in 0.until(list[i].size / 2)) {
                jobList.add(Pair(list[i][j * 2].toInt(), list[i][j * 2 + 1].toInt()))
            }
            jobs.add(jobList)
        }
    }
}
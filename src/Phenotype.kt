import java.awt.BasicStroke
import java.awt.image.BufferedImage
import kotlin.math.max
import java.io.File
import javax.imageio.ImageIO
import java.awt.Rectangle
import java.awt.Color


class Phenotype(val problem: Problem) {

    // TODO: check that schedule is never created more than once
    val schedule: MutableList<MutableList<Int>>

    init {
        // times to start jobs on
        schedule = MutableList(problem.numberJobs) { mutableListOf<Int>() }
    }

    fun fromSequenceSerial(sequence: List<Int>) {
        // TODO: put subjob in before previous subjobs on same machine if possible
        // takes sequence of jobs and makes a schedule
        for (s in schedule) {
            if (s.isNotEmpty()) {
                s.clear()
            }
        }

        // earliest time machines are available
        val machineTimes = MutableList<Int>(problem.numberJobs) { 0 }
        // next part of jobs that needs to be done
        val jobProgression = MutableList<Int>(problem.numberJobs) { 0 }
        // earliest times jobs can start next step
        val earliestJobStart = MutableList<Int>(problem.numberJobs) { 0 }

        for (s in sequence) {
            // get next step in job and update
            val nextJob = jobProgression[s]
            jobProgression[s] += 1

            // get job-machine and -time to complete
            val job = problem.jobs[s][nextJob]
            val machine = job.first
            val time = job.second
            // find earliest possible time to complete job and ad dto schedule
            val start = max(earliestJobStart[s], machineTimes[machine])
            schedule[s].add(start)

            // update machineTimes and earliestJobStart
            val duration = start + time
            machineTimes[machine] = duration
            earliestJobStart[s] = duration
        }
    }

    fun fromSequenceParallell(sequence: List<Int>) {
        // TODO: iterate through time slots instead of sequence list
    }

    fun getCost(): Int {
        if (schedule.isEmpty()) {
            println("Error: schedule is empty in Phenotype.getCost")
        }

        var time = 0
        for ((i, list) in schedule.withIndex()) {
            if (problem.jobs[i].last().second + list.last() > time) {
                time = problem.jobs[i].last().second + list.last()
            }
        }
        return time
    }

    fun plot() {
        val colorList = mutableListOf("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#800000",
            "#008000", "#000080", "#808000", "#800080", "#008080", "#808080", "#C00000", "#00C000", "#0000C0", "#C0C000",
            "#C000C0", "#00C0C0", "#C0C0C0", "#400000", "#004000", "#000040", "#404000", "#400040", "#004040", "#404040",
            "#200000", "#002000", "#000020", "#202000", "#200020", "#002020", "#202020", "#600000", "#006000", "#000060",
            "#606000", "#600060", "#006060", "#606060", "#A00000", "#00A000", "#0000A0", "#A0A000", "#A000A0", "#00A0A0",
            "#A0A0A0", "#E00000", "#00E000", "#0000E0", "#E0E000", "#E000E0", "#00E0E0", "#E0E0E0")

        val width = 26 + 10 * (getCost() + 2)
        val height = problem.numberMachines * 24 + 6 + 16
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        // draw white background
        val graph = img.createGraphics()
        graph.color = Color.WHITE
        graph.fill(Rectangle(0, 0, width, height))

        // draw machine boxes
        for (i in 0.until(problem.numberMachines)) {
            graph.color = Color.LIGHT_GRAY
            graph.fill(Rectangle(6, i * 24 + 6, 18, 18))
            graph.color = Color.BLACK
            graph.drawString("M$i", 6, (i+1) * 24 - 6)
        }

        // draw horisontal lines
        graph.color = Color.DARK_GRAY
        for (i in 0.until(problem.numberMachines + 1)) {
            graph.drawLine(26, i*24 + 3, width, i*24 + 3)
        }

        // draw vertical lines
        for (i in 0.until((width - 26)/10)) {
            if (i%5 == 0) {
                graph.color = Color.BLACK
                graph.drawString("$i", i*10 + 20, height-3)
                graph.stroke = BasicStroke(2.toFloat())
            }
            graph.color = Color.DARK_GRAY
            graph.drawLine(i*10 + 26, 3, i*10 + 26, height-3-16)
            graph.stroke = BasicStroke(1.toFloat())
        }

        // draw job boxes
        for ((i, job) in schedule.withIndex()) {
            for ((k, j) in job.withIndex()) {
                val machine = problem.jobs[i][k].first
                val time = problem.jobs[i][k].second
                graph.color = Color.decode(colorList[i])
                graph.fill(Rectangle(j*10 + 26, machine*24 + 6, time*10, 18))
                graph.color = Color.BLACK
                graph.drawString("($k/$i)", j*10 + 26, (1+machine)*24 - 6)
            }
        }
        graph.dispose()

        ImageIO.write(img, "png", File("ganttcharts/test.png"))
    }
}
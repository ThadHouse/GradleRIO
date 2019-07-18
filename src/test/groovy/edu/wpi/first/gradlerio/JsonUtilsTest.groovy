package edu.wpi.first.gradlerio

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class JsonUtilsTest extends Specification {
    @Rule TemporaryFolder testDir = new TemporaryFolder()

    File outputFile;
    File inputA;
    File inputB;


    def setup() {
        outputFile = testDir.newFile('output.json')
        testDir.newFolder('inputs')
        inputA = testDir.newFile('inputs/A.json')
        inputB = testDir.newFile('inputs/B.json')
    }

    def "Debug Info Merge Works"() {
        given:
        inputA << """
"""
        inputB << """
"""
        outputFile << "This Is Some Garbage"

        when:
        JsonUtil.mergeArrays([inputA, inputB], outputFile)

        then:
        outputFile.text == """
"""
    }

    def "External Simulation Merge Task"() {

    }
}
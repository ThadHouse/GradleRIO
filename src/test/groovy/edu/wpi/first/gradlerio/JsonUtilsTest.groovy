package edu.wpi.first.gradlerio

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class JsonUtilsTest extends Specification {
    @Rule TemporaryFolder testDir = new TemporaryFolder()

    File outputFile;
    File inputA;
    File inputB;
    File outputFileTmp;

    def setup() {
        outputFile = testDir.newFile('output.json')
        testDir.newFolder('inputs')
        inputA = testDir.newFile('inputs/A.json')
        inputB = testDir.newFile('inputs/B.json')
        outputFileTmp = testDir.newFile('output2.json')
    }

    def "Debug Info Merge Works"() {
        given:
        inputA << """[]
"""
        inputB << """[
  {
    "name": "frcUserProgram (in project cpp)",
    "extensions": [],
    "launchfile": "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\build\\\\install\\\\frcUserProgram\\\\windowsx86-64\\\\debug\\\\lib\\\\frcUserProgram.exe",
    "clang": false,
    "srcpaths": [
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\cpp"
    ],
    "headerpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\bb0fec1b21378c892475ad0451cce2b0\\\\wpilibc-cpp-2019.4.1-headers",
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\include"
    ],
    "libpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.dll"
    ],
    "debugpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.pdb"
    ],
    "libsrcpaths": []
  }
]"""
        outputFile << "This Is Some Garbage"        

        when:
        JsonUtil.mergeArrays([inputA, inputB], outputFile)

        then:
        outputFile.text == inputB.text
    }

    def "Debug Info Merge Works 2"() {
        given:
        inputA << """[
  {
    "name": "frcUserProgram (in project cpp)",
    "extensions": [],
    "launchfile": "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\build\\\\install\\\\frcUserProgram\\\\windowsx86-64\\\\debug\\\\lib\\\\frcUserProgram.exe",
    "clang": false,
    "srcpaths": [
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\cpp"
    ],
    "headerpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\bb0fec1b21378c892475ad0451cce2b0\\\\wpilibc-cpp-2019.4.1-headers",
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\include"
    ],
    "libpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.dll"
    ],
    "debugpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.pdb"
    ],
    "libsrcpaths": []
  }
]"""

        inputB << """[
  {
    "name": "frcUserProgram2 (in project cpp)",
    "extensions": [],
    "launchfile": "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\build\\\\install\\\\frcUserProgram\\\\windowsx86-64\\\\debug\\\\lib\\\\frcUserProgram.exe",
    "clang": false,
    "srcpaths": [
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\cpp"
    ],
    "headerpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\bb0fec1b21378c892475ad0451cce2b0\\\\wpilibc-cpp-2019.4.1-headers",
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\include"
    ],
    "libpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.dll"
    ],
    "debugpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.pdb"
    ],
    "libsrcpaths": []
  }
]"""
        outputFile << "This Is Some Garbage"   

        outputFileTmp << """[
  {
    "name": "frcUserProgram (in project cpp)",
    "extensions": [],
    "launchfile": "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\build\\\\install\\\\frcUserProgram\\\\windowsx86-64\\\\debug\\\\lib\\\\frcUserProgram.exe",
    "clang": false,
    "srcpaths": [
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\cpp"
    ],
    "headerpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\bb0fec1b21378c892475ad0451cce2b0\\\\wpilibc-cpp-2019.4.1-headers",
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\include"
    ],
    "libpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.dll"
    ],
    "debugpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.pdb"
    ],
    "libsrcpaths": []
  },
  {
    "name": "frcUserProgram2 (in project cpp)",
    "extensions": [],
    "launchfile": "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\build\\\\install\\\\frcUserProgram\\\\windowsx86-64\\\\debug\\\\lib\\\\frcUserProgram.exe",
    "clang": false,
    "srcpaths": [
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\cpp"
    ],
    "headerpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\bb0fec1b21378c892475ad0451cce2b0\\\\wpilibc-cpp-2019.4.1-headers",
      "C:\\\\Users\\\\test\\\\GitHub\\\\cpp\\\\src\\\\main\\\\include"
    ],
    "libpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.dll"
    ],
    "debugpaths": [
      "C:\\\\Users\\\\test\\\\.gradle\\\\caches\\\\transforms-2\\\\files-2.1\\\\f899e22fd857c79f64a4ac96bab274a8\\\\wpilibc-cpp-2019.4.1-windowsx86-64debug\\\\windows\\\\x86-64\\\\shared\\\\wpilibcd.pdb"
    ],
    "libsrcpaths": []
  }
]"""     

        when:
        JsonUtil.mergeArrays([inputA, inputB], outputFile)

        then:
        outputFile.text == outputFileTmp.text
    }

    def "External Simulation Merge Task"() {

    }
}
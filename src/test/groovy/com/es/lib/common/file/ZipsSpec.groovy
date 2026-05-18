package com.es.lib.common.file

import com.es.lib.common.file.output.OutputData
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class ZipsSpec extends Specification {

    def "Compress"() {
        when:
        def path = Paths.get('./file')
        Files.createDirectories(path)
        def items = [
                OutputData.create("test1.txt", 'text', 'Hello1'.bytes)
        ]
        then:
        Files.write(path.resolve('archive.zip'), Zips.compress(items))
    }
}

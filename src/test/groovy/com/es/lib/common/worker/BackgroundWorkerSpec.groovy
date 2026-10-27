package com.es.lib.common.worker

import spock.lang.Specification

class BackgroundWorkerSpec extends Specification {

    static class SuccessWorker extends BackgroundWorker {
        boolean worked = false

        @Override
        void run() {
            process()
        }

        @Override
        protected void doWork() {
            worked = true
        }

        @Override
        protected BackgroundWorker.Info getInfo() {
            return nameInfo("success_worker")
        }
    }

    static class ErrorWorker extends BackgroundWorker {
        Throwable capturedError

        @Override
        void run() {
            process()
        }

        @Override
        protected void doWork() {
            throw new RuntimeException("Worker failure")
        }

        @Override
        protected void doOnError(Throwable throwable) {
            capturedError = throwable
        }

        @Override
        protected BackgroundWorker.Info getInfo() {
            return nameInfo("error_worker")
        }
    }

    def "Process runs doWork on success"() {
        given:
        SuccessWorker worker = new SuccessWorker()

        when:
        worker.run()

        then:
        worker.worked
        worker.getInfo().name == "success_worker"
        worker.getInfo().toString().contains("success_worker")
    }

    def "Process catches error and calls doOnError"() {
        given:
        ErrorWorker worker = new ErrorWorker()

        when:
        worker.run()

        then:
        worker.capturedError != null
        worker.capturedError.message == "Worker failure"
    }
}

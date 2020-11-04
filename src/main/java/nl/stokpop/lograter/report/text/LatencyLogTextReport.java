/*
 * Copyright (C) 2020 Peter Paul Bakker, Stokpop Software Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.stokpop.lograter.report.text;

import nl.stokpop.lograter.analysis.ResponseTimeAnalyser;
import nl.stokpop.lograter.analysis.ResponseTimeAnalyserFailureUnaware;
import nl.stokpop.lograter.counter.RequestCounter;
import nl.stokpop.lograter.processor.latency.LatencyLogConfig;
import nl.stokpop.lograter.processor.latency.LatencyLogDataBundle;
import nl.stokpop.lograter.store.RequestCounterStorePair;
import nl.stokpop.lograter.util.time.TimePeriod;

import java.io.PrintWriter;

public class LatencyLogTextReport extends LogCounterTextReport {

    private final LatencyLogDataBundle data;

    public LatencyLogTextReport(LatencyLogDataBundle data) {
        this.data = data;
    }

    @Override
    public void report(PrintWriter out, TimePeriod analysisPeriod) {

        // latency log does not deal with failures (yet)
        // if it does, add the ResponseTimeAnalyserWithFailures!
        RequestCounter totalRequestCounter = data.getTotalRequestCounterStorePair().getRequestCounterStoreSuccess().getTotalRequestCounter();
        RequestCounter analysisRequestCounter = totalRequestCounter.getTimeSlicedCounter(analysisPeriod);

        LatencyLogConfig config = data.getConfig();
        ResponseTimeAnalyser analyserTotal = new ResponseTimeAnalyserFailureUnaware(analysisRequestCounter, analysisPeriod);

        out.println(reportSummaryHeader(analyserTotal, config));
        out.println(reportCounter(config.getCounterFields(), analyserTotal, analyserTotal, config));
        for (RequestCounterStorePair requestCounterStorePair : data.getRequestCounterStorePairs()) {
            out.println(reportCounters(config.getCounterFields(), requestCounterStorePair, analyserTotal, config));
        }
    }

    @Override
    void reportHeaderCounterDetails(StringBuilder report) {
        // no header counter details for this report
    }

}
